package mod.iceandshadow3.lib.compat.item.impl

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.item.WItemType
import mod.iceandshadow3.lib.util.collect.BinderLazy

private[lib] object BinderItem extends BinderLazy[BLogicItem, BLogicItem, AItem](new AItem(_))
