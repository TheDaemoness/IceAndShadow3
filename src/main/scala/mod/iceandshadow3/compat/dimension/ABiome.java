package mod.iceandshadow3.compat.dimension;

import mod.iceandshadow3.basics.BBiome;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;

/** A biome for an IaS3 dimension.
 */
public class ABiome extends Biome {
	ABiome(ResourceLocation dimname, BBiome b) {
		super(new BiomeBuilder().
			surfaceBuilder(new CompositeSurfaceBuilder<>(NOOP_SURFACE_BUILDER, AIR_SURFACE)).
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
