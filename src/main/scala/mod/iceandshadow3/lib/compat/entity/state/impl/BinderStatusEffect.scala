package mod.iceandshadow3.lib.compat.entity.state.impl

import mod.iceandshadow3.lib.util.collect.BinderLazy
import mod.iceandshadow3.lib.{BStatusEffect, StatusEffect}
import net.minecraft.potion.{Effect, Effects}

private[lib] object BinderStatusEffect
	extends BinderLazy[StatusEffect, BStatusEffect, Effect]({new AStatusEffect(_)})
{
	private[iceandshadow3] def populate(): Unit = {
		import mod.iceandshadow3.multiverse.misc.StatusEffects._
		//Deobfuscation on Effects seems a bit shaky, so until that gets sorted, we'll get these by ID.
		add(poison, Effects.POISON)
		add(slow, Effects.SLOWNESS)
		add(blind, Effects.BLINDNESS)
		add(wither, Effects.WITHER)
		add(regen, Effects.REGENERATION)
		add(resistance, Effects.RESISTANCE)
	}
}
