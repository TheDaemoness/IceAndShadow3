package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.BStatusEffect
import mod.iceandshadow3.util.BinderConverting

object BinderStatusEffect extends BinderConverting[BStatusEffect, AStatusEffect]({new AStatusEffect(_)})
