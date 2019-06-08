package mod.iceandshadow3.compat.item

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.util.BinderLazy

object BinderItem extends BinderLazy[BLogicItem, BLogicItem, Array[AItem]](
	logic => Array.tabulate[AItem](logic.countVariants)({new AItem(logic, _)})
)
