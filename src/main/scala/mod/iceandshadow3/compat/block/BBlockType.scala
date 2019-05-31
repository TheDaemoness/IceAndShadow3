package mod.iceandshadow3.compat.block

import net.minecraft.block.state.IBlockState

abstract class BBlockType {
	protected[compat] def state(): IBlockState
}
