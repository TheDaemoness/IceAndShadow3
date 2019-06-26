package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.state.EquipPoint._
import mod.iceandshadow3.lib.compat.entity.state.EquipPoint

case class AttackForm (
	isProjectile:Boolean = false,
	isMystic:Boolean = false, //Magic resist applies to this attack at half strength.
	relevantEquips:Array[Set[EquipPoint]] = Array(Set(BODY_HEAD), Set(BODY_CHEST), Set(BODY_LEGS), Set(BODY_CHEST)),
	dimensions:Int = 2 //Number of dimensions a cross-section of the attack has.
)
object AttackForm {
	val SWING = AttackForm(dimensions = 1)
	val STAB = AttackForm(
		dimensions = 0,
		relevantEquips = Array(Set(BODY_CHEST))
	)
	val BALL = AttackForm(isProjectile = true)
	val MISSILE = AttackForm(
		isProjectile = true,
		dimensions = 0
	)
	val MISSILE_MAGIC = AttackForm(
		isProjectile = true,
		isMystic = true,
		dimensions = 0
	)
	val THROWN = AttackForm(
		isProjectile = true,
		dimensions = 1
	)
	val CONDITION = AttackForm(
		dimensions = 3
	)
	val CURSE = AttackForm(
		isMystic = true,
		dimensions = 3
	)
	val FLOOR = AttackForm(
		relevantEquips = Array(Set(BODY_FEET))
	)
	val CEILING = AttackForm(
		relevantEquips = Array(Set(BODY_HEAD))
	)
	val TERRAIN = AttackForm(
		relevantEquips = Array(Set(BODY_LEGS, BODY_FEET))
	)
	val VOLUME = AttackForm(
		relevantEquips = Array(Set(BODY_HEAD, BODY_CHEST, BODY_LEGS))
	)
	val ELDRITCH = AttackForm(
		dimensions = 4,
		relevantEquips = Array()
	)
}