package mod.iceandshadow3.lib.entity

import mod.iceandshadow3.lib.StatusEffect

//TODO: Maybe make into a sealed trait (mainly to support tick variance).
case class Status private(effect: StatusEffect, ticks: Int, amp: Int, ambient: Boolean)

object Status {
	def byTicks(
		effect: StatusEffect,
		ticks: Int,
		amp: Int = 1,
		ambient: Boolean = true
	): Status = Status(effect, ticks, amp, ambient)
	def byIntervals(
		effect: StatusEffect,
		intervals: Float,
		amp: Int = 1,
		ambient: Boolean = true
	): Status = Status(effect, (effect.intervalTicks(amp)*intervals).toInt, amp, ambient)
}
