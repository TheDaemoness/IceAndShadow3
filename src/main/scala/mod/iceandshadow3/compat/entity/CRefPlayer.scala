package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.compat.item.{CInventory, CRefItem}
import mod.iceandshadow3.compat.world.CDimension
import mod.iceandshadow3.util.{IteratorConcat, L3, Vec3}
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentTranslation
import scala.collection.JavaConverters._

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

	def donateToEnderChest(what: CRefItem): L3 = {
		if(findItem(what, false).isEmpty) {
			new CInventory(player.getInventoryEnderChest).donate(what)
		} else L3.NEUTRAL
	}

	def advancement(name: String, criteria: String*): Unit = player match {
		case mp: EntityPlayerMP =>
			val what = mp.getServer.getAdvancementManager.getAdvancement(new ResourceLocation(name))
			if(what == null) {
				IaS3.logger.warn(s"Advancement with id $name does not exist.")
				return
			}
			val advancements = mp.getAdvancements
			for(critname <- what.getCriteria.keySet().asScala) {
				if(criteria.isEmpty || criteria.contains(name)) advancements.grantCriterion(what, critname)
			}
		case _ =>
	}
}
