package mod.iceandshadow3.lib.compat.entity.state

import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.item.WItemStack
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.inventory.EquipmentSlotType._

sealed abstract class EquipPoint {
	protected[entity] def getItem(who: LivingEntity): WItemStack
}
case class EquipPointVanilla(where: EquipmentSlotType) extends EquipPoint {
	override protected[entity] def getItem(who: LivingEntity): WItemStack =
		new WItemStack(who.getItemStackFromSlot(where), who)
}
abstract class BEquipPointVirtual extends EquipPoint {
	protected def getItem(whose: WEntityLiving): WItemStack
	override protected[entity] def getItem(who: LivingEntity) = getItem(CNVEntity.wrap(who))
}
object EquipPoint {
	val HAND_MAIN = EquipPointVanilla(MAINHAND)
	val HAND_OFF = EquipPointVanilla(OFFHAND)
	val BODY_HEAD = EquipPointVanilla(HEAD)
	val BODY_CHEST = EquipPointVanilla(CHEST)
	val BODY_LEGS = EquipPointVanilla(LEGS)
	val BODY_FEET = EquipPointVanilla(FEET)
	val USING = new EquipPoint {
		override protected[entity] def getItem(who: LivingEntity) = new WItemStack(who.getActiveItemStack, who)
	}
	val USING_SHIELD = new EquipPoint {
		override protected[entity] def getItem(who: LivingEntity) = {
			val is = who.getActiveItemStack
			if(is.isShield(who)) new WItemStack(is, who)
			else WItemStack.empty
		}
	}
}