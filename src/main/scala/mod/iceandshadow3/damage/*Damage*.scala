package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.state.BStatus
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityLiving}

abstract class BDamage {
	def amount(target: WEntity): Float
	def name: String
	def applyPre(target: WEntityLiving, multiplier: Float): Unit = ()
	def applyPost(target: WEntityLiving, multiplier: Float): Unit = ()
}

//Minor departure from the IaS3 naming scheme.
abstract class Damage(damage: Float) extends BDamage {
	final override def amount(target: WEntity) = damage
	def name: String
}

abstract class DamageWithStatus(damage: Float, val statuses: BStatus*) extends Damage(damage) {
	override def applyPost(target: WEntityLiving, multiplier: Float): Unit = for(status <- statuses) status(target)
}