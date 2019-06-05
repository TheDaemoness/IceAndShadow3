package mod.iceandshadow3.compat.dimension;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.compat.CNVVec3;
import mod.iceandshadow3.spatial.IPosChunk;
import mod.iceandshadow3.spatial.IPosColumn;
import mod.iceandshadow3.spatial.IVec3;
import mod.iceandshadow3.util.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class AModDimension extends ModDimension {
	private final BDimension dimlogic; //Couldn't resist.
	private final ResourceLocation name;
	public final ABiome dimbiome;
	public AModDimension(BDimension what) {
		dimlogic = what;
		name = new ResourceLocation(IaS3.MODID, what.name());
		this.setRegistryName(name);
		this.dimbiome = new ABiome(name, what);
	}
	public void enable() {
		//Forge marks the registry for internal use only.
		//I'd like to know how else they want us to getColumn DimensionTypes out of DimensionManager.
		if(DimensionManager.getRegistry().func_212607_c(name)) {
			dimlogic.coord_$eq(WDimensionCoord.apply(DimensionManager.getRegistry().func_212608_b(name)));
		} else {
			DimensionType dimtype = DimensionManager.registerDimension(name, this, null);
			//TODO: The third arg can be null, but probably shouldn't be.
			dimlogic.coord_$eq(WDimensionCoord.apply(dimtype));
		}
	}
	//TODO: We probably need a disable function for server unload.
	public BDimension getIaSDimension() {
		return dimlogic;
	}

	class ADimension extends Dimension {
		private final DimensionType type;
		ADimension(DimensionType type) {
			this.type = type;
		}

		@Override
		protected void init() {
			this.hasSkyLight = dimlogic.getSkyBrightness(0f) >= 0f;
			this.world.setSkylightSubtracted(world.calculateSkylightSubtracted(1.0f));
		}

		@Nonnull
		@Override
		public IChunkGenerator<?> createChunkGenerator() {
			return new AChunkGenerator(this.world, dimlogic, dimbiome);
		}

		@Nullable
		public BlockPos getSpawnCoordinate() {
			final IVec3 where = dimlogic.getWorldSpawn();
			if(where == null) return null;
			else return CNVVec3.toBlockPos(where);
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
				public long xBlock() {return x;}

				@Override
				public long zBlock() {return z;}
			});
		}

		@Override
		public float getSunBrightnessFactor(float partialTicks) {
			return dimlogic.getSkyBrightness(partialTicks);
		}

		@OnlyIn(Dist.CLIENT)
		@Override
		public float getSunBrightness(float partialTicks) {
			return dimlogic.getSkyBrightness(partialTicks);
		}

		@Nonnull
		@Override
		public DimensionType getType() {
			return type;
		}

		//TODO: Way incomplete.
	}
	@Override
	public Function<DimensionType, ? extends Dimension> getFactory() {
		return (mctype) -> this.new ADimension(mctype);
	}
}
