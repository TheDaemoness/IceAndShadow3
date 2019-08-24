package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.block.{BBlockVar, BlockVar}
import mod.iceandshadow3.lib.util.collect.BinderEarly
import net.minecraft.block.BlockState
import net.minecraft.state.IProperty

private[lib] object BinderBlockVar extends BinderEarly[BlockVar[_], BBlockVar[_], IProperty[_]](
	logic => new AProperty(logic)
) {
	def applyAndCast(variable: BBlockVar[_]): AProperty = apply(variable).asInstanceOf[AProperty]
	def update[T](state: BlockState, variable: BBlockVar[T], what: T) =
		state.`with`[Integer, Integer](apply(variable).asInstanceOf[AProperty], variable(what))
}
