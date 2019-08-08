package mod.iceandshadow3.lib.compat.entity.state.impl

import mod.iceandshadow3.lib.util.collect.BinderLazy
import mod.iceandshadow3.lib.{BStatusEffect, StatusEffect}
import net.minecraft.potion.Effect

private[lib] object BinderStatusEffect
	extends BinderLazy[StatusEffect, BStatusEffect, Effect]({new AStatusEffect(_)})
{
	private[iceandshadow3] def populate(): Unit = {
		import mod.iceandshadow3.multiverse.misc.Statuses._
		//Deobfuscation on Effects seems a bit shaky, so until that gets sorted, we'll get these by ID.
		add(poison, Effect.get(19))
		add(slow, Effect.get(2))
		add(blind, Effect.get(15))
		add(wither, Effect.get(20))
		add(regen, Effect.get(10))
		add(resistance, Effect.get(11))
	}
}
