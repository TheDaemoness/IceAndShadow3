package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.entity.WEntityLiving
import mod.iceandshadow3.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.util.{Color, E3vl}

sealed abstract class StatusEffect extends BinderStatusEffect.TKey {}

final class StatusEffectPlaceholder extends StatusEffect {}

abstract class BStatusEffect(val name: String, val isBeneficial: E3vl, val color: Color)
	extends StatusEffect
{
	BinderStatusEffect.add(this)
	def onStart(who: WEntityLiving, amp: Int)
	def shouldTick(duration: Int, amp: Int): Boolean
	def onTick(who: WEntityLiving, amp: Int)
	def onEnd(who: WEntityLiving, amp: Int)
}
