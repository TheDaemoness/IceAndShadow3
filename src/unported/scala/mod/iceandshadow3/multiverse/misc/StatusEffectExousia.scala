package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.damage.{Attack, AttackForm, DamageSimple, TDmgTypeExousic}
import mod.iceandshadow3.lib.StatusEffectIntervaled
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.util.{Color, E3vl}

class StatusEffectExousia extends StatusEffectIntervaled("exousia", E3vl.FALSE, new Color(0x6ee1ad)) {
	val damage = Attack("exousia", AttackForm.VOLUME, new DamageSimple(2f) with TDmgTypeExousic)
	override def intervalTicks(amp: Int) = 30/amp
	override def doTick(who: WEntityLiving, amp: Int): Unit = damage(who)
}
