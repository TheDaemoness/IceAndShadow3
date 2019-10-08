package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.WEntity

abstract class BDamage {
	def amount(target: WEntity): Float
	def name: String
}

//Minor departure from the IaS3 naming scheme.
abstract class Damage(damage: Float) extends BDamage {
	final def amount(target: WEntity) = damage
	def name: String
}