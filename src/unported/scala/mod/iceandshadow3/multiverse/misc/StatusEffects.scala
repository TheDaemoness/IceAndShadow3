package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.lib.StatusEffectPlaceholder

object StatusEffects {
	def init(): Unit = ()

	val poison = new StatusEffectPlaceholder(50 >> _)
	val slow = new StatusEffectPlaceholder
	val blind = new StatusEffectPlaceholder
	val wither = new StatusEffectPlaceholder(80 >> _)
	val regen = new StatusEffectPlaceholder(100 >> _)
	val resistance = new StatusEffectPlaceholder
	//NOTE: Make sure the above also in BinderStatusEffect.populate

	val frost = new StatusEffectFrost
	val exousia = new StatusEffectExousia
	val renewal = new StatusEffectRenewal
}
