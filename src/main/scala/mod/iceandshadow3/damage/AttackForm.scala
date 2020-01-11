package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.state.BEquipPoint
import mod.iceandshadow3.lib.compat.entity.state.BEquipPoint._

case class AttackForm (
	relevantArmor:Array[Set[BEquipPoint]] = Array(Set(BODY_HEAD), Set(BODY_CHEST), Set(BODY_LEGS), Set(BODY_CHEST)),
	ranged:Boolean = true,
	blockable:Boolean = true,
	projectile:Boolean = false,
	mystic:Boolean = false,
	volumetric:Boolean = false //Damages shields regardless of whether their armor contributes to DR or not.
)
object AttackForm {
	private val ARMORLESS = Array(Set[BEquipPoint]())
	val SWING = AttackForm(
		ranged = false
	)
	val STAB = AttackForm(
		ranged = false,
		relevantArmor = Array(Set(BODY_CHEST))
	)
	val MISSILE = AttackForm(
		projectile = true
	)
	val MISSILE_MAGIC = AttackForm(
		projectile = true,
		mystic = true
	)
	val CONDITION = AttackForm(
		ranged = false,
		blockable = false,
		relevantArmor = ARMORLESS
	)
	val CURSE = AttackForm(
		mystic = true,
		relevantArmor = ARMORLESS
	)
	val FLOOR = AttackForm(
		ranged = false,
		blockable = false,
		relevantArmor = Array(Set(BODY_FEET))
	)
	val CEILING = AttackForm(
		ranged = false,
		relevantArmor = Array(Set(BODY_HEAD))
	)
	val WAVE = AttackForm(
		volumetric = true,
		relevantArmor = Array(Set(BODY_HEAD, BODY_CHEST, BODY_LEGS, BODY_FEET))
	)
	val VOLUME = AttackForm(
		volumetric = true,
		blockable = false,
		relevantArmor = Array(Set(BODY_HEAD, BODY_CHEST, BODY_LEGS, BODY_FEET))
	)
	val ELDRITCH = AttackForm(
		blockable = false,
		relevantArmor = ARMORLESS
	)
}