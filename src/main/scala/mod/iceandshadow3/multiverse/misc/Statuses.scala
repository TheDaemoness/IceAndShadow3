package mod.iceandshadow3.multiverse.misc

import mod.iceandshadow3.basics.StatusEffectPlaceholder

object Statuses {
	def init(): Unit = ()

	val poison = new StatusEffectPlaceholder
	val slow = new StatusEffectPlaceholder
	val blind = new StatusEffectPlaceholder
	val wither = new StatusEffectPlaceholder
	val regen = new StatusEffectPlaceholder
	val resistance = new StatusEffectPlaceholder
	//NOTE: Make sure these are also in BinderStatusEffect.populate

	val frost = new StatusFrost
}
