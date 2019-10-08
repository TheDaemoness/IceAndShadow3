package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.lib.StatusEffectPlaceholder

object StatusEffects {
	def init(): Unit = ()

	//NOTE: Make sure these are also in BinderStatusEffect.populate

	val poison = new StatusEffectPlaceholder {
		override def intervalTicks(amp: Int) = 50 >> amp
	}
	val slow = new StatusEffectPlaceholder
	val blind = new StatusEffectPlaceholder
	val wither = new StatusEffectPlaceholder {
		override def intervalTicks(amp: Int) = 80 >> amp
	}
	val regen = new StatusEffectPlaceholder {
		override def intervalTicks(amp: Int) = 100 >> amp
	}
	val resistance = new StatusEffectPlaceholder

	val frost = new StatusEffectFrost
	val exousia = new StatusEffectExousia
}
