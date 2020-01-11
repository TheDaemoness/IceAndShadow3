package mod.iceandshadow3.lib.compat.world.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.LogicDimension;
import mod.iceandshadow3.lib.compat.block.WBlockView;
import mod.iceandshadow3.lib.compat.util.CNVCompat$;
import mod.iceandshadow3.lib.compat.world.WDimensionCoord;
import mod.iceandshadow3.lib.compat.world.WWorld;
import mod.iceandshadow3.lib.spatial.IPosBlock;
import mod.iceandshadow3.lib.spatial.IPosChunk;
import mod.iceandshadow3.lib.spatial.IPosColumn;
import mod.iceandshadow3.lib.util.Color;
import mod.iceandshadow3.lib.world.BHandlerFog;
import mod.iceandshadow3.lib.world.BHandlerSky;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AModDimension extends ModDimension {
	private static final Map<AModDimension, LogicDimension> reverseMap = new HashMap<>();
	public static LogicDimension lookup(final ModDimension dim) {
		if(dim instanceof AModDimension) return reverseMap.getOrDefault(dim, null);
		else return null;
	}
	private final LogicDimension dimlogic; //Couldn't resist.
	private final ResourceLocation name;
	public final ABiome dimbiome;
	public AModDimension(LogicDimension what) {
		dimlogic = what;
		name = new ResourceLocation(IaS3.MODID, what.name());
		this.setRegistryName(name);
		this.dimbiome = new ABiome(name, what);
		reverseMap.put(this, what);
	}
	@SuppressWarnings("deprecation")
	public void enable() {
		//Forge marks the registry for internal use only.
		//I'd like to know how else they want us to get DimensionTypes out of DimensionManager besides register.
		DimensionType dimtype;
		dimtype = DimensionManager.getRegistry().getOrDefault(name);
		if(dimtype == null) {
			dimtype = DimensionManager.registerDimension(
				name, this,
				null, //TODO: Can be null, but probably shouldn't.
				dimlogic.handlerSky().hasLuma()
			);
		} else IaS3.logger().debug("Dimension "+name+" is already enabled. Using registry value.");
		dimlogic.coord_$eq(WDimensionCoord.apply(dimtype));
	}

	//TODO: We probably need a disable function for server unload.
	public LogicDimension getIaSDimension() {
		return dimlogic;
	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		return ADimension::new;
	}


	private static Vec3d fromColor(Color color, Vec3d ifNull) {
		return color != null ? new Vec3d(color.red(), color.green(), color.blue()) : ifNull;
	}

	class ADimension extends Dimension {
		private final WWorld worldWrapped;
		private final DimensionType type;
		private final BHandlerFog fog;
		private final BHandlerSky sky;
		private final Vec3d fogColor;
		private final Vec3d skyColor;

		ADimension(World w, DimensionType type) {
			super(w, type);
			this.type = type;
			worldWrapped = new WWorld(w);
			this.fog = dimlogic.handlerFog();
			this.sky = dimlogic.handlerSky();
			fogColor = fromColor(fog.colorDefault(), new Vec3d(0, 0, 0));
			skyColor = fromColor(sky.colorDefault(), new Vec3d(0, 0, 0));
		}

		@Override
		public BlockPos getSpawnPoint() {
			return CNVCompat$.MODULE$.toBlockPos(dimlogic.getWorldSpawn(worldWrapped));
		}

		@Override
		public boolean hasSkyLight() {
			return sky.hasLuma();
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public float getStarBrightness(float partialTicks) {
			return sky.stars(worldWrapped, partialTicks);
		}

		@Nonnull
		@Override
		public ChunkGenerator<?> createChunkGenerator() {
			return new AChunkGenerator(this.world, dimlogic, dimbiome);
		}

		@Nullable
		public BlockPos getSpawnCoordinate() {
			final IPosBlock where = dimlogic.getWorldSpawn(worldWrapped);
			if(where == null) return null;
			else return CNVCompat$.MODULE$.toBlockPos(where);
		}

		@Override
		public boolean canDoRainSnowIce(Chunk chunk) {
			return false;
		}

		@Override
		public boolean canDoLightning(Chunk chunk) {
			return false;
		}

		@Override
		public DimensionType getRespawnDimension(ServerPlayerEntity player) {
			return dimlogic.getRespawnDim().dimtype();
		}

		@Nullable
		private BlockPos doFindSpawn(IPosColumn xz, boolean checkValid) {
			final IPosBlock where = dimlogic.findSpawn(worldWrapped, xz);
			if(!checkValid || dimlogic.checkSpawn(new WBlockView(worldWrapped.exposeWorld(), where))) {
				return new BlockPos(where.xBlock(), where.yBlock(), where.zBlock());
			} else return null;
		}

		@Override
		public int getMoonPhase(long worldTime) {
			return sky.moon(worldWrapped, worldTime).id;
		}

		@Override
		public boolean isDaytime() {
			return sky.isDay(worldWrapped);
		}

		@Nullable
		@Override
		public BlockPos findSpawn(@Nonnull ChunkPos cp, boolean checkValid) {
			return doFindSpawn(dimlogic.findSpawn(worldWrapped, new IPosChunk() {
				@Override
				public int xChunk() { return cp.x; }
				@Override
				public int zChunk() { return cp.z; }
			}), checkValid);
		}

		@Override
		public double getHorizon() {
			return dimlogic.peakLevel();
		}

		@Nullable
		@Override
		public BlockPos findSpawn(int x, int z, boolean checkValid) {
			return doFindSpawn(IPosColumn.wrap(x, z), checkValid);
		}

		@OnlyIn(Dist.CLIENT)
		public float getCloudHeight() {
			return dimlogic.cloudLevel();
		}

		@Override
		public float calculateCelestialAngle(long worldTime, float partialTicks) {
			return sky.angle(worldWrapped, worldTime, partialTicks);
		}

		@Override
		public boolean isSurfaceWorld() {
			return dimlogic.isSurface();
		}

		@Nonnull
		@Override
		public Vec3d getFogColor(float skyAngle, float partialTicks) {
			return fromColor(fog.colorDynamic(worldWrapped, skyAngle, partialTicks), fogColor);
		}

		@Override
		public Vec3d getSkyColor(BlockPos cameraPos, float partialTicks) {
			return fromColor(sky.colorDynamic(
				worldWrapped,
				cameraPos.getX(), cameraPos.getY(), cameraPos.getZ(),
				partialTicks
			), skyColor);
		}

		@Override
		public boolean canRespawnHere() {
			return dimlogic.getRespawnDim() == dimlogic.coord();
		}

		@Override
		public boolean doesXZShowFog(int x, int z) {
			return fog.hasFogAt(worldWrapped, x, z);
		}

		@Override
		public float getSunBrightness(float partialTicks) {
			return sky.luma(worldWrapped, partialTicks);
		}

		@Override
		protected void generateLightBrightnessTable() {
			dimlogic.brightnessTable(lightBrightnessTable);
		}

		@Override
		public void getLightmapColors(float partTicks, float sunLuma, float skyLight, float blockLight, float[] colors) {
			final Color result = dimlogic.modifyLightmap(skyLight, blockLight, new Color(colors[0], colors[1], colors[2]));
			colors[0] = result.red();
			colors[1] = result.green();
			colors[2] = result.blue();
		}

		@Nonnull
		@Override
		public DimensionType getType() {
			return type;
		}

		//TODO: Way incomplete.
	}
}
