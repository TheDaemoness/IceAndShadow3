package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.lib.util.collect.UniSeq

class NyxColumnIsleCentral(x: Int, z: Int, chunk: NyxChunk)
extends BNyxColumnIsleMountainSnowy(x, z, chunk, x == 0 && z == 0) {

	override protected def stoneLower: WBlockState = WorldGenNyx.stones(0)

	override protected def height() = {
		val bias = Math.max(islevalue*4 - 3, 0d)
		MathUtils.interpolate(super.height(), bias, 128).toFloat
	}

	override protected def caves() = new UniSeq(255, false)
}
