package mod.iceandshadow3.world.misc

import mod.iceandshadow3.basics.StatusEffectPlaceholder

object Statuses {
	val poison = new StatusEffectPlaceholder
	val slow = new StatusEffectPlaceholder
	val blind = new StatusEffectPlaceholder
	val wither = new StatusEffectPlaceholder
	//NOTE: Make sure these are also in BinderStatusEffect.populate

	//WARNING: Begin IaS3 statuses. DO NOT REORDER!
	val frost = new StatusFrost
}
