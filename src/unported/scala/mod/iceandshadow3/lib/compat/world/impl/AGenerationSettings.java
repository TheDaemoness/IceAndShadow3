package mod.iceandshadow3.lib.compat.world.impl;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationSettings;

import javax.annotation.Nonnull;

public class AGenerationSettings extends GenerationSettings {
	@Override public int getVillageDistance() { return 0; }
	@Override public int getVillageSeparation() { return 0; }
	@Override public int getOceanMonumentSpacing() { return 0; }
	@Override public int getOceanMonumentSeparation() { return 0; }
	@Override public int getStrongholdDistance() { return 0; }
	@Override public int getStrongholdCount() { return 0; }
	@Override public int getStrongholdSpread() { return 0; }
	@Override public int getBiomeFeatureDistance() { return 0; }
	@Override public int getBiomeFeatureSeparation() { return 0; }
	@Override public int getEndCityDistance() { return 0; }
	@Override public int getEndCitySeparation() { return 0; }
	@Override public int getMansionDistance() { return 0; }
	@Override public int getMansionSeparation() { return 0; }
	@Nonnull
	@Override public BlockState getDefaultBlock() { return Blocks.AIR.getDefaultState(); }
	@Nonnull
	@Override public BlockState getDefaultFluid() { return Blocks.AIR.getDefaultState(); }

	public static final AGenerationSettings instance = new AGenerationSettings();
}
