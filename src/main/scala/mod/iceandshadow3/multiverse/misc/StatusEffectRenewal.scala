package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.lib.BStatusEffectIntervaled
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.entity.state.WDamage
import mod.iceandshadow3.lib.util.{Color, E3vl}

class StatusEffectRenewal extends BStatusEffectIntervaled("renewal", E3vl.TRUE, new Color(0x78c266)) {
	override def intervalTicks(amp: Int) = 24
	override def doTick(who: WEntityLiving, amp: Int): Unit = {
		if(who.hp >= who.hpMax) who.remove(this)
		else who.heal(amp)
	}
	override def onHarm(who: WEntityLiving, how: WDamage, amp: Int) = {
		who.remove(this)
		how.severity
	}
}
