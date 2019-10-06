package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.{BlockTypeSnow, CommonBlockTypes}
import mod.iceandshadow3.lib.gen.{BWorldGenLayerDecoration, BWorldGenLayerTerrain, WorldGenColumn}
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.multiverse.DomainNyx

class NyxWorldGenLayerSnowAndIce(seed: Long, icicleInfrequency: Int) extends BWorldGenLayerDecoration(DomainNyx) {
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get
	def apply(in: WorldGenColumn): Unit = {
		val hasIcicles = in.rng.nextInt(icicleInfrequency) == 0
		var doSnow = true
		var doIcicles = hasIcicles
		val height = in(BWorldGenLayerTerrain.varHeight)+1
		var y = 256
		while(y > 32) {
			y -= 1
			if(doSnow) {
				import WorldGenNyx._
				if (in(y - 1) != CommonBlockTypes.AIR) {
					doSnow = false
					val delta = height - y
					val snowmod = MathUtils.ratioBelow(yFull, y, yThinning)
					in.update(y, if (snowmod != 0) {
						val snowdelta = if (smoothsnow) delta else if (delta > 2f / 3) 5d / 7 else 1d / 7
						BlockTypeSnow.fromFloat(snowmod * snowdelta).asWBlockState
					} else null.asInstanceOf[WBlockState])
				}
			} else if(doIcicles) {
				if(in(y) == CommonBlockTypes.AIR) {
					doIcicles = false
					//TODO: Check if it can stay here.
					in.update(y, WorldGenNyx.icicles)
				}
			} else return
		}
	}
}
