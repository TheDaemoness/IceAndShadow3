package mod.iceandshadow3.lib.compat.block.`type`

import mod.iceandshadow3.lib.block.BBlockFn
import mod.iceandshadow3.lib.compat.block.WBlockState
import net.minecraft.block.BlockState

/** Block state factory base class.
	*/
trait TBlockStateSource {
	protected[compat] def exposeBS(): BlockState
	def asWBlockState: WBlockState = new WBlockState(exposeBS())
	implicit def asBlockFn: BBlockFn = (x: Int, y: Int, z: Int, in: WBlockState) => asWBlockState
}
