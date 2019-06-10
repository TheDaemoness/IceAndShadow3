package mod.iceandshadow3.compat.dimension;

import mod.iceandshadow3.basics.BBiome;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

/** A biome for an IaS3 dimension.
 */
class ABiome extends Biome {
	ABiome(ResourceLocation dimname, BBiome b) {
		super(new Biome.Builder().
			surfaceBuilder(new ConfiguredSurfaceBuilder<>(SurfaceBuilder.field_215407_R, SurfaceBuilder.field_215422_s)).
			category(Category.NONE).
			downfall(b.baseDownfall()).
			temperature(b.baseTemperature()).
			waterColor(b.baseWaterColor().colorcode()).
			waterFogColor(b.baseWaterFogColor().colorcode()).
			depth(b.baseAltitude()).scale(b.baseHilliness()).
			precipitation(
				b.baseDownfall() <= 0.2 ? RainType.NONE : (b.baseTemperature() <= 0.2 ? RainType.SNOW : RainType.RAIN)
			).parent(null)
		);
		this.setRegistryName(dimname);
	}
}
