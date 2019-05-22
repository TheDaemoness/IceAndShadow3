package mod.iceandshadow3.basics.damage

import mod.iceandshadow3.compat.entity.EquipPoint
import mod.iceandshadow3.compat.entity.EquipPoint._

case class DamageForm (
	isProjectile:Boolean = false,
	isBlast:Boolean = false,
	isMystic:Boolean = false,
	relevantEquips:Set[EquipPoint] = Set(BODY_HEAD, BODY_CHEST, BODY_LEGS),
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
		relevantEquips = Set(BODY_FEET)
	)
	val CEILING = DamageForm(
		relevantEquips = Set(BODY_HEAD)
	)
	val ELDRITCH = DamageForm(dimensions = 4)
}