package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.{BStatusEffect, StatusEffect}
import mod.iceandshadow3.util.BinderLazy
import net.minecraft.potion.Potion

object BinderStatusEffect extends BinderLazy[StatusEffect, BStatusEffect, Potion]({new AStatusEffect(_)})
