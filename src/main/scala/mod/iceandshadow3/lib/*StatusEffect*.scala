package mod.iceandshadow3.lib

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.base.INamed
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.lib.util.{Color, E3vl}

sealed abstract class StatusEffect extends BinderStatusEffect.TKey {
	def intervalTicks(amp: Int): Int = 20
}

class StatusEffectPlaceholder extends StatusEffect

abstract class BStatusEffect(val name: String, val isBeneficial: E3vl, val color: Color)
extends StatusEffect
with INamed {
	BinderStatusEffect.add(this)
	ContentLists.status.add(this)

	override def toString = s"$name (status)"

	def onStart(who: WEntityLiving, amp: Int): Unit
	def shouldTick(duration: Int, amp: Int): Boolean
	def onTick(who: WEntityLiving, amp: Int): Unit
	def onEnd(who: WEntityLiving, amp: Int): Unit

	override final def getNames = Array(name)
}

abstract class BStatusEffectIntervaled(name: String, isBeneficial: E3vl, color: Color)
extends BStatusEffect(name, isBeneficial, color) {
	def shouldTick(duration: Int, amp: Int) = (duration % intervalTicks(amp)) == 0
}
