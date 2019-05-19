package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.util.Vec3
import net.minecraft.entity.player.EntityPlayer
import scala.collection.JavaConverters._

//TODO: Manually generated class stub.
class CRefPlayer(player: EntityPlayer) extends CRefLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f
	def deshield(force: Boolean = true): Unit = player.disableShield(force)
	def bed: Vec3 = player.bedLocation

	override def findItem(itemid: String, restrictToHands: Boolean = false): CRefItem = {
		val lookingfor = CRefItem.make(itemid, player)
		if(lookingfor.isEmpty) return lookingfor
		if(restrictToHands) {
			val option = List(equipment(EquipPoint.HAND_MAIN),equipment(EquipPoint.HAND_OFF)).find{lookingfor.matches(_)}
			return option.getOrElse(new CRefItem(null, player))
		} else {
			val is = player.inventory.mainInventory.asScala.find {lookingfor.matches(_)}
			new CRefItem(is.orNull, player)
		}
	}
}
