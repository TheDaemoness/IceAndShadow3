package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.compat.block.`type`.{BBlockType, BlockType, BlockTypeSnow}
import mod.iceandshadow3.lib.util.MathUtils

abstract class BColumnMountainSnowy(x: Int, z: Int, region: RegionInterpret, voidhole: Boolean)
extends BColumnMountain(x, z, region, voidhole) {

	override protected def stoneUpper: BlockType = WorldGenNyx.stones(0)

	def icicleInfrequency = 24

	override protected def decorate(in: Array[BBlockType], height: Float, rng: Random): Unit = {
		val hasIcicles = rng.nextInt(icicleInfrequency) == 0
		var doSnow = true
		var doIcicles = hasIcicles
		for (yminus <- -254 to -32) {
			val y = -yminus
			if(doSnow) {
				import WorldGenNyx._
				if (in(y - 1) != null) {
					doSnow = false
					val delta = height - y
					val snowmod = MathUtils.ratioBelow(yFull, y, yThinning)
					in(y) = if (snowmod != 0) {
						val snowdelta = if (region.smoothsnow) delta else if (delta > 2f / 3) 5d / 7 else 1d / 7
						BlockTypeSnow.fromFloat(snowmod * snowdelta)
					} else null
				}
			} else if(doIcicles) {
				if(in(y) == null) {
					doIcicles = false
					//TODO: Check if it can stay here.
					in(y) = WorldGenNyx.icicles
				}
			} else return
		}
	}
}
