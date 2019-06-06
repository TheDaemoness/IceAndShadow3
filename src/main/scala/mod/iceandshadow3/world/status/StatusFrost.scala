package mod.iceandshadow3.world.status

import mod.iceandshadow3.basics.BStatusEffect
import mod.iceandshadow3.compat.entity.WEntityLiving
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeCold}
import mod.iceandshadow3.util.{Color, E3vl}

class StatusFrost extends BStatusEffect("frost", E3vl.FALSE, new Color(0x5079ff)) {
	val damage = new Attack("frostbite", AttackForm.CONDITION, new BDamage with TDmgTypeCold {
		override def baseDamage = 1f
		override def onDamage(dmg: Float, dmgResisted: Float, what: WRefItem) = dmgResisted
	})
	override def onStart(who: WEntityLiving, amp: Int): Unit = {}
	override def shouldTick(duration: Int, amp: Int) = {
		(duration % 30) == 0
	}
	override def onTick(who: WEntityLiving, amp: Int): Unit = {
		val dmg = if(who.sprinting) amp-1 else amp
		if(dmg <= 0) return
		who.damage(damage, dmg)
	}
	override def onEnd(who: WEntityLiving, amp: Int): Unit = {}
}
