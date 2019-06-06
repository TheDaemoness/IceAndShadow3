package mod.iceandshadow3.compat.item

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.util.BinderConverting

object BinderItem extends BinderConverting[BLogicItem, BLogicItem, Array[AItem]](
	logic => Array.tabulate[AItem](logic.countVariants)({new AItem(logic, _)})
)
