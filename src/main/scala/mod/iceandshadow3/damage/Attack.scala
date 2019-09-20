package mod.iceandshadow3.damage

case class Attack(name: String, form: AttackForm, instances: Damage with TDmgTypeOmni*) {
	val baseDamage = instances.foldLeft(0f)((total, dmg) => dmg.baseDamage+total)
	def baseDamage(multiplier: Float): Float = baseDamage*multiplier
	def determineMultiplier(dmg: Float): Float = dmg/baseDamage
}