package mod.iceandshadow3.lib.compat.entity.state

import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import net.minecraft.entity.LivingEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.inventory.EquipmentSlotType._

sealed abstract class EquipPoint {
	protected[entity] def getItem(who: LivingEntity): WItemStackOwned[WEntityLiving]
}
case class EquipPointVanilla(where: EquipmentSlotType) extends EquipPoint {
	override protected[entity] def getItem(who: LivingEntity): WItemStackOwned[WEntityLiving] =
		new WItemStackOwned(who.getItemStackFromSlot(where), CNVEntity.wrap(who))
}
class EquipPointVirtual(val getItem: WEntityLiving => WItemStackOwned[WEntityLiving]) extends EquipPoint {
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
		override protected[entity] def getItem(who: LivingEntity) =
			new WItemStackOwned(who.getActiveItemStack, CNVEntity.wrap(who))
	}
	val USING_SHIELD = new EquipPoint {
		override protected[entity] def getItem(who: LivingEntity) = {
			val is = who.getActiveItemStack
			if(is.isShield(who)) new WItemStackOwned(is, CNVEntity.wrap(who))
			else WItemStack.empty.withOwner(CNVEntity.wrap(who))
		}
	}
}