package mod.iceandshadow3.compat.block

import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.compat.item.AItemBlock
import mod.iceandshadow3.util.BinderConverting

object BinderBlock extends BinderConverting[BLogicBlock, Array[(ABlock, AItemBlock)]](
	logic => Array.tabulate[(ABlock, AItemBlock)](logic.countVariants)(variant => {
		val ablock = new ABlock(logic, variant)
		val iblock = if(!logic.isTechnical(variant)) new AItemBlock(logic, variant, ablock) else null
		(ablock, iblock)
	})
)
