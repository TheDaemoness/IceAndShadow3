package mod.iceandshadow3.compat.block.`type`

import net.minecraft.block.BlockState

/** Block state factory base class.
	*/
abstract class BBlockType {
	protected[compat] def state(): BlockState
}
