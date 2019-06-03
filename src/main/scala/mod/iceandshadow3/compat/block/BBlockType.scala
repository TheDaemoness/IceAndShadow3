package mod.iceandshadow3.compat.block

import net.minecraft.block.state.IBlockState

/** Block state factory base class.
	*/
abstract class BBlockType {
	protected[compat] def state(): IBlockState
}
