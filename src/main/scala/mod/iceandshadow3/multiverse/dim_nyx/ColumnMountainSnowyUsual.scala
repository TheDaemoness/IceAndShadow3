package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.util.MathUtils

class ColumnMountainSnowyUsual(x: Int, z: Int, region: RegionInterpret)
extends BColumnMountainSnowy(x, z, region, false) {

	override protected def stoneLower: WBlockState =
		WorldGenNyx.stoneCommon(region.stoneMapLower(x, z).makeRandomXZ(region.noises.seed, 40201))

	override protected def caves() = {
		import WorldGenNyx._
		val fissures = region.fissuremap(x,z)
		val caves = region.cavemap(x,z)
		val caveLimitReal = Math.min(yCaveMax, height-4).toInt
		Array.tabulate[Boolean](255)(y => {
			val fissureAttenUpper = Math.sqrt(MathUtils.ratioBelow(yFissureFull, y, yFissureMax))
			val fissureAttenLower = 1-MathUtils.ratioBelow((height*0.6f).toInt, y, (height*0.8f).toInt)
			val caveAtten = MathUtils.ratioBelow(Math.max(0, caveLimitReal - 10), y, caveLimitReal)
			//WARNING: Short-circuit evaluation.
			y < yFissureMax && fissures(y)*fissureAttenLower > (1 - fissureAttenUpper * 0.2) ||
				y < yCaveMax && caves(y) > (1 - caveAtten * 0.2)
		}
		).toIndexedSeq
	}
}
