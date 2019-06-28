package mod.iceandshadow3.lib.compat.item.impl

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.util.collect.BinderLazy

private[lib] object BinderItem extends BinderLazy[BLogicItem, BLogicItem, Array[AItem]](
	logic => Array.tabulate[AItem](logic.countVariants)({new AItem(logic, _)})
)