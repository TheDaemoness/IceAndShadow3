package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.{BStatusEffect, StatusEffect}
import mod.iceandshadow3.util.BinderLazy
import net.minecraft.init.MobEffects
import net.minecraft.potion.Potion

object BinderStatusEffect extends BinderLazy[StatusEffect, BStatusEffect, Potion]({new AStatusEffect(_)}) {
	private[iceandshadow3] def populate(): Unit = {
		import mod.iceandshadow3.world.misc.Statuses._
		add(poison, MobEffects.POISON)
		add(slow, MobEffects.SLOWNESS)
		add(blind, MobEffects.BLINDNESS)
		add(wither, MobEffects.WITHER)
	}
}
