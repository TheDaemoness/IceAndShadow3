package mod.iceandshadow3.lib

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.lib.util.INamed
import mod.iceandshadow3.util.{Color, E3vl}

sealed abstract class StatusEffect extends BinderStatusEffect.TKey {}

final class StatusEffectPlaceholder extends StatusEffect {}

abstract class BStatusEffect(val name: String, val isBeneficial: E3vl, val color: Color)
	extends StatusEffect
	with INamed
{
	BinderStatusEffect.add(this)
	ContentLists.status.add(this)

	override def toString = s"$name (status)"

	def onStart(who: WEntityLiving, amp: Int): Unit
	def shouldTick(duration: Int, amp: Int): Boolean
	def onTick(who: WEntityLiving, amp: Int): Unit
	def onEnd(who: WEntityLiving, amp: Int): Unit

	override final def getNames = Array(name)
}
