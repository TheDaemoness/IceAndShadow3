package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.lib.util.collect.UniSeq
import mod.iceandshadow3.multiverse.dim_nyx.{NyxTerrainMaps, WorldGenNyx}

class NyxColumnIsleCentral(x: Int, z: Int, chunk: NyxTerrainMaps)
extends BNyxColumnIsleMountain(x, z, chunk) {
	override protected def stoneLower: WBlockState = WorldGenNyx.stones(0)

	override protected def genHeight() = {
		val bias = Math.max(islevalue*4 - 3, 0d)
		MathUtils.interpolate(super.genHeight(), bias, 128).toFloat
	}

	override protected def caves() = new UniSeq(255, false)
}
