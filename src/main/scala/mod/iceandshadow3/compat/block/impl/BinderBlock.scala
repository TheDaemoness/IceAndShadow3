package mod.iceandshadow3.compat.block.impl

import mod.iceandshadow3.basics.BLogicBlock
import mod.iceandshadow3.compat.item.impl.AItemBlock
import mod.iceandshadow3.util.BinderLazy

object BinderBlock extends BinderLazy[BLogicBlock, BLogicBlock, Array[(ABlock, AItemBlock)]](
	logic => Array.tabulate[(ABlock, AItemBlock)](logic.countVariants)(variant => {
		val ablock = new ABlock(logic, variant)
		val iblock = if(!logic.isTechnical) new AItemBlock(logic, variant, ablock) else null
		(ablock, iblock)
	})
)