package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.entity.{BinderStatusEffect, WEntityLiving}
import mod.iceandshadow3.util.{Color, E3vl}

abstract class BStatusEffect(val name: String, val isBeneficial: E3vl, val color: Color)
	extends BinderStatusEffect.TKey
{
	BinderStatusEffect.add(this)
	def onStart(who: WEntityLiving, amp: Int)
	def shouldTick(duration: Int, amp: Int): Boolean
	def onTick(who: WEntityLiving, amp: Int)
	def onEnd(who: WEntityLiving, amp: Int)
}
