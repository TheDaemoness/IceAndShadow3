package mod.iceandshadow3.compat.world.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.compat.CNVVec3;
import mod.iceandshadow3.compat.world.WDimensionCoord;
import mod.iceandshadow3.spatial.IPosChunk;
import mod.iceandshadow3.spatial.IPosColumn;
import mod.iceandshadow3.spatial.IVec3;
import mod.iceandshadow3.util.Color;
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
	private static final Map<AModDimension, BDimension> reverseMap = new HashMap<>();
	public static BDimension lookup(final ModDimension dim) {
		if(dim instanceof AModDimension) return reverseMap.getOrDefault(dim, null);
		else return null;
	}
	private final BDimension dimlogic; //Couldn't resist.
	private final ResourceLocation name;
	public final ABiome dimbiome;
	public AModDimension(BDimension what) {
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
		if(DimensionManager.getRegistry().containsKey(name)) {
			dimtype = DimensionManager.getRegistry().getOrDefault(name);
		} else {
			dimtype = DimensionManager.registerDimension(
				name, this,
				null, //TODO: Can be null, but probably shouldn't.
				dimlogic.getSkyBrightness(-1f) >= 0f
			);
		}
		dimlogic.coord_$eq(WDimensionCoord.apply(dimtype));
	}

	//TODO: We probably need a disable function for server unload.
	public BDimension getIaSDimension() {
		return dimlogic;
	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		return ADimension::new;
	}

	class ADimension extends Dimension {
		private final DimensionType type;
		ADimension(World w, DimensionType type) {
			super(w, type);
			this.type = type;
		}

		@Override
		public boolean hasSkyLight() {
			return dimlogic.getSkyBrightness(-1f) >= 0f;
		}

		@Nonnull
		@Override
		public ChunkGenerator<?> createChunkGenerator() {
			return new AChunkGenerator(this.world, dimlogic, dimbiome);
		}

		@Nullable
		public BlockPos getSpawnCoordinate() {
			final IVec3 where = dimlogic.getWorldSpawn();
			if(where == null) return null;
			else return CNVVec3.toBlockPos(where);
		}

		@Override
		public boolean canDoRainSnowIce(Chunk chunk) {
			return false;
		}

		@Override
		public boolean canDoLightning(Chunk chunk) {
			return false;
		}

		@Nullable
		@Override
		public BlockPos findSpawn(ChunkPos cp, boolean checkValid) {
			return findSpawn(cp.x, cp.z, checkValid);
		}

		@Nullable
		@Override
		public BlockPos findSpawn(int x, int z, boolean checkValid) {
			final IVec3 where = dimlogic.findSpawn(new IPosChunk() {
				@Override
				public int xChunk() {return x;}

				@Override
				public int zChunk() {return z;}
			}, checkValid);
			if(where == null) return null;
			else return CNVVec3.toBlockPos(where);
		}

		@OnlyIn(Dist.CLIENT)
		public float getCloudHeight() {
			return dimlogic.cloudLevel();
		}

		@Override
		public float calculateCelestialAngle(long worldTime, float partialTicks) {
			return dimlogic.skyAngle(worldTime, partialTicks);
		}

		@Override
		public boolean isSurfaceWorld() {
			return false; //Setting this true exposes us to a LOT of default behaviors.
		}

		@Nonnull
		@Override
		public Vec3d getFogColor(float skyAngle, float partialTicks) {
			final Color color = dimlogic.fogColor(skyAngle, partialTicks);
			return new Vec3d(color.red(), color.green(), color.blue());
		}

		@Override
		public boolean canRespawnHere() {
			return dimlogic.getWorldSpawn() != null;
		}

		@Override
		public boolean doesXZShowFog(int x, int z) {
			return dimlogic.hasFogAt(new IPosColumn() {
				@Override
				public long xBlock() {
					return x;
				}

				@Override
				public long zBlock() {
					return z;
				}
			});
		}

		@Override
		public float getSunBrightness(float partialTicks) {
			return dimlogic.getSkyBrightness(partialTicks);
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
