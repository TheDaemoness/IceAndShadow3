package mod.iceandshadow3.lib.compat.entity.state

import mod.iceandshadow3.lib.StatusEffect
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import net.minecraft.potion.EffectInstance

abstract class BStatus {
	def getEffect: StatusEffect
	def getTicks: Int
	def getAmp: Int
	def isAmbient: Boolean = false
	final def apply(who: WEntityLiving): Unit = if(who.isServerSide) {
		val ticks = getTicks
		val amp = getAmp
		val effect = getEffect
		if(amp <= 0) who.remove(effect)
		else if(ticks > 0) who.living.addPotionEffect(new EffectInstance(
			BinderStatusEffect(effect),
			ticks,
			amp-1,
			isAmbient,
			true
		))
	}
}
