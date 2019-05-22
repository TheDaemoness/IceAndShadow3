package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.util.{IteratorConcat, Vec3}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextComponentTranslation

import scala.collection.JavaConverters._

//TODO: Manually generated class stub.
class CRefPlayer(player: EntityPlayer) extends CRefLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f
	def deshield(force: Boolean = true): Unit = player.disableShield(force)
	def bed: Vec3 = player.bedLocation
	def message(msg: String, actionBar: Boolean = true): Unit
		= player.sendStatusMessage(new TextComponentTranslation(msg), actionBar)

	override def findItem(itemid: String, restrictToHands: Boolean = false): CRefItem = {
		val lookingfor = CRefItem.make(itemid, player)
		if(lookingfor.isEmpty) return lookingfor
		if(restrictToHands) {
			val option = List(equipment(EquipPoint.HAND_MAIN),equipment(EquipPoint.HAND_OFF)).find{lookingfor.matches(_)}
			option.getOrElse(new CRefItem(null, player))
		} else {
			val is = player.inventory.mainInventory.asScala.find {lookingfor.matches(_)}
			new CRefItem(is.orNull, player)
		}
	}

	override def items(): Iterator[CRefItem] = {
		val inv = player.inventory
		new IteratorConcat[ItemStack, CRefItem](
			(is: ItemStack) => {new CRefItem(is, player)},
			inv.offHandInventory.iterator,
			inv.armorInventory.iterator,
			inv.mainInventory.iterator
		)
	}
}
