package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx.{yCaveMax, yFissureFull, yFissureMax}
import mod.iceandshadow3.multiverse.dim_nyx.{NyxTerrainMaps, WorldGenNyx}

class NyxColumnIsleMountainUsual(x: Int, z: Int, chunk: NyxTerrainMaps)
extends BNyxColumnIsleMountain(x, z, chunk) {

	override protected def stoneLower: WBlockState = if(islevalue > 0.25) {
		val source = chunk.stoneLower(x, z)
		val distance = MathUtils.fastMag(cell.cellClosest.xAbs, cell.cellClosest.zAbs)
		WorldGenNyx.stones({
			if(distance < 2) source.common
			else if(distance == 2) source.uncommon
			else source.rare
		}.ordinal())
	} else WorldGenNyx.stones(0)

	val caveLimitReal = Math.min(yCaveMax, genHeight - 4).toInt
	val fissureStrength = 0.3 - islevalue/10
	val caveStrength = 0.15 + islevalue/10

	private val caveAt: Int => Boolean = y => {
		//WARNING: Fissure map heights are NOT 255.
		val fissureAttenUpper = Math.sqrt(MathUtils.ratioBelow(yFissureFull, y, yFissureMax))
		val fissureAttenLower = 1 - MathUtils.ratioBelow((genHeight * 0.6f).toInt, y, (genHeight * 0.8f).toInt)
		val caveAtten = MathUtils.ratioBelow(Math.max(0, caveLimitReal - 10), y, caveLimitReal)
		//WARNING: Short-circuit evaluation.
		y < yFissureMax && chunk.fissure(x, y, z) * fissureAttenLower > (1 - fissureAttenUpper * fissureStrength) ||
			y < yCaveMax && chunk.cave(x, y, z) > (1 - caveAtten * caveStrength)	}

	override protected def caves() = {
		Array.tabulate[Boolean](255)(caveAt).toIndexedSeq
	}
}
