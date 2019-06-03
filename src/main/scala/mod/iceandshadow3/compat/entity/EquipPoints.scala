package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.item.WRefItem
import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.inventory.EntityEquipmentSlot._

sealed abstract class EquipPoint {
	protected[entity] def getItem(who: EntityLivingBase): WRefItem
}
case class EquipPointVanilla(where: EntityEquipmentSlot) extends EquipPoint {
	override protected[entity] def getItem(who: EntityLivingBase): WRefItem =
		new WRefItem(who.getItemStackFromSlot(where), who)
}
object EquipPoint {
	val HAND_MAIN = EquipPointVanilla(MAINHAND)
	val HAND_OFF = EquipPointVanilla(OFFHAND)
	val BODY_HEAD = EquipPointVanilla(HEAD)
	val BODY_CHEST = EquipPointVanilla(CHEST)
	val BODY_LEGS = EquipPointVanilla(LEGS)
	val BODY_FEET = EquipPointVanilla(FEET)
}