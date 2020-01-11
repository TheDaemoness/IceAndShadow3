package mod.iceandshadow3.lib.block

import mod.iceandshadow3.lib.compat.block.WBlockState

abstract class BlockFn {
	def apply(x: Int, y: Int, z: Int, in: WBlockState): WBlockState
}
