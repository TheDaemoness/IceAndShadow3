package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.item.impl.AItemBlock
import mod.iceandshadow3.lib.util.collect.BinderLazy

private[lib] object BinderBlock extends BinderLazy[
	BLogicBlock, BLogicBlock, (LogicBlockAdapters.Adapter, AItemBlock[_])
] (logic => {
		val ablock = LogicBlockAdapters(logic)
		val iblock = logic.itemLogic.fold[AItemBlock[_]](null)(new AItemBlock(_, ablock))
		(ablock, iblock)
}) {
	override def onUnboundApply(ias: BinderBlock.TKey) = null

	override def freeze() = {
		val retval = super.freeze()
		LogicBlockAdapters.disable()
		retval
	}
}
