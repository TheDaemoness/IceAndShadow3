package mod.iceandshadow3.world.status

import mod.iceandshadow3.basics.StatusEffectPlaceholder
import mod.iceandshadow3.compat.entity.BinderStatusEffect
import net.minecraft.init.MobEffects

object Statuses {
	val poison = new StatusEffectPlaceholder
	val slow = new StatusEffectPlaceholder
	val blind = new StatusEffectPlaceholder
	val wither = new StatusEffectPlaceholder
	//NOTE: Make sure these are also in BinderStatusEffect.addVanillaEffects

	//WARNING: Begin IaS3 statuses. DO NOT REORDER!
	val frost = new StatusFrost
}
