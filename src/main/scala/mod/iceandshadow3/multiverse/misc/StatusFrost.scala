package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeCold}
import mod.iceandshadow3.lib.BStatusEffect
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.util.{Color, E3vl}

class StatusFrost extends BStatusEffect("frost", E3vl.FALSE, new Color(0x5079ff)) {
	val damage = new Attack("frostbite", AttackForm.CONDITION, new BDamage with TDmgTypeCold {
		override def baseDamage = 1f
		override def onDamage(dmg: Float, dmgResisted: Float, what: WItemStack) = dmgResisted
	})
	override def onStart(who: WEntityLiving, amp: Int): Unit = {}
	override def shouldTick(duration: Int, amp: Int) = {
		(duration % 30) == 0
	}
	override def onTick(who: WEntityLiving, amp: Int): Unit = {
		val dmg = if(who.sprinting) amp-1 else {
			//who.setStatus(Statuses.regen, 0) //TODO: Decide between this or just reducing healing.
			amp
		}
		if(dmg <= 0) return
		who.damage(damage, dmg)
	}
	override def onEnd(who: WEntityLiving, amp: Int): Unit = {}
}
