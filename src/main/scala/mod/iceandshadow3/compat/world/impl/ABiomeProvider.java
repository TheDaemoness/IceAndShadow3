package mod.iceandshadow3.compat.world.impl;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ABiomeProvider extends BiomeProvider {
	public final Biome abiome;
	public ABiomeProvider(ABiome ab) {
		abiome = ab;
	}
	@Override
	public Biome getBiome(int x, int z) {
		return abiome;
	}

	@Nonnull
	public Biome[] getBiomes(int startX, int startZ, int xSize, int zSize) {
		final Biome[] retval = new Biome[xSize * zSize];
		Arrays.fill(retval, abiome);
		return retval;
	}

	@Nonnull
	@Override
	public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
		return getBiomes(x, z, width, length);
	}

	@Override
	public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
		return new HashSet<>(Collections.singletonList(abiome));
	}

	@Nullable
	@Override
	public BlockPos findBiomePosition(int x, int z, int range, @Nonnull List<Biome> biomes, @Nonnull Random random) {
		return biomes.contains(abiome) ? new BlockPos(x, 0, z) : null;
	}

	@Override
	public boolean hasStructure(@Nonnull Structure<?> structureIn) {
		return false;
	}

	@Override
	public Set<BlockState> getSurfaceBlocks() {
		return Collections.emptySet();
	}
}