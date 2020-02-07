package mod.iceandshadow3.lib.compat.world.impl;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.structure.Structure;

import javax.annotation.Nonnull;
import java.util.*;

public class ABiomeProvider extends BiomeProvider {
	public final Biome abiome;
	public ABiomeProvider(ABiome ab) {
		super(Collections.singleton(ab));
		abiome = ab;
	}

	@Nonnull
	@Override
	public Biome func_225526_b_(int x, int y, int z) {
		return abiome;
	}

	@Nonnull
	@Override
	public List<Biome> getBiomesToSpawnIn() {
		return Collections.singletonList(abiome);
	}

	@Override
	public boolean hasStructure(@Nonnull Structure<?> structureIn) {
		return false;
	}

	@Nonnull
	@Override
	public Set<BlockState> getSurfaceBlocks() {
		return Collections.emptySet();
	}
}