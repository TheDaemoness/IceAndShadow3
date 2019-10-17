package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.damage.{Attack, AttackForm, Damage, TDmgTypeExousic}
import mod.iceandshadow3.lib.BStatusEffectIntervaled
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.util.{Color, E3vl}

class StatusEffectExousia extends BStatusEffectIntervaled("exousia", E3vl.FALSE, new Color(0x6ee1ad)) {
	val damage = Attack("exousia", AttackForm.VOLUME, new Damage(2f) with TDmgTypeExousic)
	override def intervalTicks(amp: Int) = 30/amp
	override def doTick(who: WEntityLiving, amp: Int): Unit = damage(who)
}
