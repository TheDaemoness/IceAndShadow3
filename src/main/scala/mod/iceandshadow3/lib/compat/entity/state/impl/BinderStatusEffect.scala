package mod.iceandshadow3.lib.compat.entity.state.impl

import mod.iceandshadow3.lib.{BStatusEffect, StatusEffect}
import mod.iceandshadow3.util.collect.BinderLazy
import net.minecraft.potion.Effect

private[lib] object BinderStatusEffect
	extends BinderLazy[StatusEffect, BStatusEffect, Effect]({new AStatusEffect(_)})
{
	private[iceandshadow3] def populate(): Unit = {
		import mod.iceandshadow3.multiverse.misc.Statuses._
		//Deobfuscation on Effects seems a bit shaky, so until that gets sorted, we'll get these by ID.
		add(poison, Effect.getPotionById(19))
		add(slow, Effect.getPotionById(2))
		add(blind, Effect.getPotionById(15))
		add(wither, Effect.getPotionById(20))
		add(regen, Effect.getPotionById(10))
		add(resistance, Effect.getPotionById(11))
	}
}