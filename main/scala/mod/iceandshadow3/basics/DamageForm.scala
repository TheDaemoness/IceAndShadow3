package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.entity.EquipPoint

case class DamageForm (
	isProjectile:Boolean = false,
	isBlast:Boolean = false,
	isMystic:Boolean = false,
	relevantEquips:List[EquipPoint] = List(EquipPoint.HEAD, EquipPoint.CHEST, EquipPoint.LEGS),
	dimensions:Int = 2 //Number of dimensions a cross-section of the attack has.
)
object DamageForm {
	val SWING = DamageForm(dimensions = 1)
	val STAB = DamageForm(dimensions = 0)
	val BALL = DamageForm(isProjectile = true)
	val MISSILE = DamageForm(
		isProjectile = true,
		dimensions = 0
	)
	val THROWN = DamageForm(
		isProjectile = true,
		dimensions = 1
	)
	val CURSE = DamageForm(
		isMystic = true,
		dimensions = 3
	)
	val FLOOR = DamageForm(
		relevantEquips = List(EquipPoint.FEET)
	)
	val CEILING = DamageForm(
		relevantEquips = List(EquipPoint.HEAD)
	)
	val ELDRITCH = DamageForm(dimensions = 4)
}