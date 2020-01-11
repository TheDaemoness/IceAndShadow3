package mod.iceandshadow3.lib.compat.item.impl

import mod.iceandshadow3.lib.LogicItem
import mod.iceandshadow3.lib.compat.item.WItemType
import mod.iceandshadow3.lib.util.collect.BinderLazy

private[lib] object BinderItem extends BinderLazy[LogicItem, LogicItem, AItem](new AItem(_))
