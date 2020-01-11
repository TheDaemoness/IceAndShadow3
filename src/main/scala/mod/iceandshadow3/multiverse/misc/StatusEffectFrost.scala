package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.damage.{Attack, AttackForm, DamageSimple, TDmgTypeCold}
import mod.iceandshadow3.lib.StatusEffectIntervaled
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.util.{Color, E3vl}

class StatusEffectFrost extends StatusEffectIntervaled("frost", E3vl.FALSE, new Color(0x5079ff)) {
	val damage = Attack("frostbite", AttackForm.CONDITION, new DamageSimple(1f) with TDmgTypeCold)
	override def intervalTicks(amp: Int) = 30
	override def doTick(who: WEntityLiving, amp: Int): Unit = {
		val dmg = if(who.sprinting) amp-1 else {
			//who.clearStatus(Statuses.regen) //TODO: Decide between this or just reducing healing.
			amp
		}
		if(dmg <= 0) return
		damage.*(dmg)(who)
	}
}
