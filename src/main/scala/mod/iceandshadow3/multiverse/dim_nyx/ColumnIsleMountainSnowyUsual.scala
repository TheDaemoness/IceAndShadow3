package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.util.MathUtils

class ColumnIsleMountainSnowyUsual(x: Int, z: Int, chunk: NyxChunk)
extends BColumnIsleMountainSnowy(x, z, chunk, false) {

	override protected def stoneLower: WBlockState =
		WorldGenNyx.stoneCommon(chunk.stoneLower(x, z).makeRandomXZ(chunk.noises.seed, 40201))

	override protected def caves() = {
		import WorldGenNyx._
		val caveLimitReal = Math.min(yCaveMax, height - 4).toInt
		Array.tabulate[Boolean](255)(y => {
			//WARNING: Fissure map heights are NOT 255.
			val fissureAttenUpper = Math.sqrt(MathUtils.ratioBelow(yFissureFull, y, yFissureMax))
			val fissureAttenLower = 1 - MathUtils.ratioBelow((height * 0.6f).toInt, y, (height * 0.8f).toInt)
			val caveAtten = MathUtils.ratioBelow(Math.max(0, caveLimitReal - 10), y, caveLimitReal)
			//WARNING: Short-circuit evaluation.
			y < yFissureMax && chunk.fissure(x, y, z) * fissureAttenLower > (1 - fissureAttenUpper * 0.2) ||
				y < yCaveMax && chunk.cave(x, y, z) > (1 - caveAtten * 0.2)
		}).toIndexedSeq
	}
}
