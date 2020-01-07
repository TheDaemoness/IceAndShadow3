package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.block.{BlockQueries, WBlockState}
import mod.iceandshadow3.lib.compat.block.`type`.{BlockTypeSnow, CommonBlockTypes}
import mod.iceandshadow3.lib.gen.{BWorldGenLayerDecoration, BWorldGenLayerTerrain, WorldGenColumn}
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx.{yBald, yThinning}

class NyxWorldGenLayerSurface(seed: Long, icicleInfrequency: Int) extends BWorldGenLayerDecoration(DomainNyx) {
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get

	protected def thickSnow(height: Float, y: Int): WBlockState = {
		if (y > yBald) null
		else {
			if (height <= yThinning) BlockTypeSnow.SNOWS.last.asWBlockState
			else {
				val snowmod = MathUtils.ratioBelow(yThinning, y, yBald)
				if (snowmod != 0) BlockTypeSnow.fromFloat(snowmod).asWBlockState else null
			}
		}
	}

	def apply(in: WorldGenColumn): Unit = {
		val hasIcicles = in.rng.nextInt(icicleInfrequency) == 0
		var doIcicles = hasIcicles
		val height = in(BWorldGenLayerTerrain.varHeight)+1
		var y = yBald+1
		// DO NOT INCREASE. ALGORITHM DOES NOT SUPPORT DEEPER SNOW.
		var snows = 2
		while(y > 32) {
			y -= 1
			if(snows > 0) {
				import WorldGenNyx._
				val support = in(y - 1)
				if (support != CommonBlockTypes.AIR) {
					snows -= 1
					val canSnow = if(snows > 0 && BlockQueries.solid(support)) {
						snows -= 1
						if(in(y) == CommonBlockTypes.AIR) {
							in.update(y, thickSnow(height, y))
							y += 1
						}
						true
					} else in(y) == CommonBlockTypes.AIR
					if(canSnow) {
						val delta = height - y
						val snowmod = MathUtils.ratioBelow(yFull, y, yThinning)
						in.update(y, if (snowmod != 0) {
							val snowdelta = if (smoothsnow) delta else if (delta > 2f / 3) 5d / 7 else 1d / 7
							BlockTypeSnow.fromFloat(snowmod * snowdelta).asWBlockState
						} else null.asInstanceOf[WBlockState])
					}
				}
			} else if(doIcicles) {
				if(in(y) == CommonBlockTypes.AIR) {
					doIcicles = false
					if(BlockQueries.solid(in(y+1))) in.update(y, WorldGenNyx.icicles)
				}
			} else return
		}
	}
}
