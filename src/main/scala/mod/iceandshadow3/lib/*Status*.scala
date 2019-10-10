package mod.iceandshadow3.lib

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.base.INamed
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.entity.state.{BStatus, WDamage}
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.lib.util.{Color, E3vl}

sealed abstract class StatusEffect extends BinderStatusEffect.TKey {
	def intervalTicks(amp: Int): Int
	final val inactive = new BStatus {
		override def getEffect = StatusEffect.this
		override def getTicks = 0
		override def getAmp = 0
	}
	final def forTicks(ticks: Int, amp: Int = 1, ambient: Boolean = false) = new BStatus {
		override def getEffect = StatusEffect.this
		override def getTicks = ticks
		override def getAmp = amp
		override def isAmbient = ambient
	}
	final def forIntervals(intervals: Float, amp: Int = 1, ambient: Boolean = false) =
		forTicks((this.intervalTicks(amp)*intervals).toInt, amp, ambient)
}

final class StatusEffectPlaceholder(ticksForAmp: Int => Int = _ => 20) extends StatusEffect {
	override def intervalTicks(amp: Int) = ticksForAmp(amp)
}

abstract class BStatusEffect(val name: String, val isBeneficial: E3vl, val color: Color)
extends StatusEffect
with INamed {
	BinderStatusEffect.add(this)
	ContentLists.status.add(this)

	def onStart(who: WEntityLiving, amp: Int): Unit
	def shouldTick(duration: Int, amp: Int): Boolean
	def onTick(who: WEntityLiving, amp: Int): Unit
	def onEnd(who: WEntityLiving, amp: Int): Unit
	/** Called when an entity is harmed with this status effect active.
		* @return The damage they should take. Return 0 for no damage, or a negative number to heal from the damage. */
	def onHarm(who: WEntityLiving, how: WDamage, amp: Int) = how.severity

	override final def getNames = Array(name)
	override def intervalTicks(amp: Int) = 20
	override def toString = s"$name (status)"
}

abstract class BStatusEffectIntervaled(name: String, isBeneficial: E3vl, color: Color)
extends BStatusEffect(name, isBeneficial, color) {
	def shouldTick(duration: Int, amp: Int) = (duration % intervalTicks(amp)) == 0
}