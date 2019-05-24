package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.compat.item.{CInventory, CRefItem}
import mod.iceandshadow3.compat.world.CDimension
import mod.iceandshadow3.util.{IteratorConcat, Vec3}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextComponentTranslation

//TODO: Manually generated class stub.
class CRefPlayer protected[entity](protected[compat] val player: EntityPlayer) extends CRefLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f
	def deshield(force: Boolean = true): Unit = player.disableShield(force)
	def bed: Vec3 = player.bedLocation
	def message(msg: String, actionBar: Boolean = true): Unit
		= player.sendStatusMessage(new TextComponentTranslation(msg), actionBar)

	override def home(where: CDimension): Option[Vec3] =
		Option(player.getBedLocation(where.dimensionCoord.dimtype)).fold(super.home(where)){pos => Option(fromBlockPos(pos))}

	override def isCreative = player.isCreative

	override def items(): Iterator[CRefItem] = {
		val inv = player.inventory
		new IteratorConcat[ItemStack, CRefItem](
			(is: ItemStack) => {new CRefItem(is, player)},
			inv.offHandInventory.iterator,
			inv.armorInventory.iterator,
			inv.mainInventory.iterator
		)
	}

	override def saveItem(what: CRefItem): Boolean =
		new CInventory(player.getInventoryEnderChest).add(what)

	def donateToEnderChest(what: CRefItem): Boolean = {
		//Possible TODO: Optimize.
		if(findItem(what, false).isEmpty) {
			new CInventory(player.getInventoryEnderChest).donate(what)
		}
		else false
	}
}
