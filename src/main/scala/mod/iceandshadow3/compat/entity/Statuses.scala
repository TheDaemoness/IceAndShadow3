package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.StatusEffectPlaceholder
import net.minecraft.init.MobEffects
import mod.iceandshadow3.world.status._

object Statuses {
	val poison = new StatusEffectPlaceholder
	val slow = new StatusEffectPlaceholder
	val blind = new StatusEffectPlaceholder

	//WARNING: Begin IaS3 statuses. DO NOT REORDER!
	val frost = new StatusFrost

	def addVanillaEffects(): Unit = {
		import BinderStatusEffect._
		add(poison, MobEffects.POISON)
		add(slow, MobEffects.SLOWNESS)
		add(blind, MobEffects.BLINDNESS)
	}
}
