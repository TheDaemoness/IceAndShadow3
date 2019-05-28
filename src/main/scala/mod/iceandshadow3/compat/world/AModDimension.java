package mod.iceandshadow3.compat.world;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.compat.Vec3Conversions;
import mod.iceandshadow3.util.Vec3;
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
	public AModDimension(BDimension what) {
		dimlogic = what;
		this.setRegistryName(IaS3.MODID, what.name());
		//TODO: Remove the following explosion when it's ready.
		throw new IllegalArgumentException("IaS3 dimensions are NOT READY FOR USE!");
	}
	public void register() {
		dimlogic.coord_$eq(CDimensionCoord.apply(DimensionManager.registerDimension(
			this.getRegistryName(), //SHOULD BE NON-NULL AFTER CONSTRUCTION!
			this,
			null
		)));
		//TODO: The third arg can be null, but probably shouldn't be.
	}

	class ADimension extends Dimension {
		private final DimensionType type;
		ADimension(DimensionType type) {
			this.type = type;
		}

		@Override
		protected void init() {
			this.hasSkyLight = dimlogic.hasSkyLight();
		}

		@Override
		public IChunkGenerator<?> createChunkGenerator() {
			return null; //TODO: Implement world generator.
		}

		@Nullable
		public BlockPos getSpawnCoordinate() {
			final Vec3 where = dimlogic.getWorldSpawn();
			if(where == null) return null;
			else return Vec3Conversions.toBlockPos(where);
		}

		@Nullable
		@Override
		public BlockPos findSpawn(ChunkPos cp, boolean checkValid) {
			return findSpawn(cp.x, cp.z, checkValid);
		}

		@Nullable
		@Override
		public BlockPos findSpawn(int xChunk, int zChunk, boolean checkValid) {
			return Vec3Conversions.toBlockPos(dimlogic.findSpawn(xChunk, zChunk, checkValid));
		}

		@OnlyIn(Dist.CLIENT)
		public float getCloudHeight() {
			return dimlogic.cloudHeight();
		}

		@Override
		public float calculateCelestialAngle(long worldTime, float partialTicks) {
			return dimlogic.skyAngle(worldTime, partialTicks);
		}

		@Override
		public boolean isSurfaceWorld() {
			return false; //Setting this true exposes us to a LOT of default behaviors.
		}

		@Override
		public Vec3d getFogColor(float p_76562_1_, float partialTicks) {
			return null; //TODO: Color class.
		}

		@Override
		public boolean canRespawnHere() {
			return dimlogic.getWorldSpawn() != null;
		}

		@Override
		public boolean doesXZShowFog(int x, int z) {
			return dimlogic.hasFogAt(x, z);
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
