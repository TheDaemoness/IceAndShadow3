package mod.iceandshadow3.lib.compat.block.impl

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.item.WItemType
import mod.iceandshadow3.lib.compat.item.impl.AItemBlock
import mod.iceandshadow3.lib.util.collect.BinderLazy

private[lib] object BinderBlock extends BinderLazy[BLogicBlock, BLogicBlock, (ABlock, AItemBlock)](
	logic => {
		val ablock = new ABlock(logic)
		val iblock = logic.itemLogic.fold[AItemBlock](null)(new AItemBlock(_, ablock))
		(ablock, iblock)
	}
) {
	override def onUnboundApply(ias: BinderBlock.TKey) = null
}
