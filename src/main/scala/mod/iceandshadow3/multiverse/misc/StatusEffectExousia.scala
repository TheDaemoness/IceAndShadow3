package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.damage.{Attack, AttackForm, Damage, TDmgTypeExousic}
import mod.iceandshadow3.lib.BStatusEffectIntervaled
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.util.{Color, E3vl}

class StatusEffectExousia extends BStatusEffectIntervaled("exousia", E3vl.FALSE, new Color(0x6ee1ad)) {
	val damage = new Attack("exousia", AttackForm.VOLUME, new Damage(2f) with TDmgTypeExousic)
	override def onStart(who: WEntityLiving, amp: Int): Unit = {}
	override def intervalTicks(amp: Int) = 30/amp
	override def onTick(who: WEntityLiving, amp: Int): Unit = {
		who.damage(damage, amp)
	}
	override def onEnd(who: WEntityLiving, amp: Int): Unit = {}
}