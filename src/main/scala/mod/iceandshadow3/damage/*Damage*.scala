package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.state.Status
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityLiving}

abstract class Damage {
	def amount(target: WEntity): Float
	def name: String
	def applyPre(target: WEntityLiving, multiplier: Float): Unit = ()
	def applyPost(target: WEntityLiving, multiplier: Float): Unit = ()
}

//Minor departure from the IaS3 naming scheme.
abstract class DamageSimple(damage: Float) extends Damage {
	final override def amount(target: WEntity) = damage
	def name: String
}

abstract class DamageWithStatus(damage: Float, val statuses: Status*) extends DamageSimple(damage) {
	override def applyPost(target: WEntityLiving, multiplier: Float): Unit = for(status <- statuses) status(target)
}