package mod.iceandshadow3.damage

class Attack(val name: String, val form: AttackForm, instances: BDamage*) {
	val baseDamage = instances.foldLeft(0f)((total, dmg) => dmg.baseDamage+total)
	def baseDamage(multiplier: Float): Float = baseDamage*multiplier
	def determineMultiplier(dmg: Float): Float = dmg/baseDamage
}