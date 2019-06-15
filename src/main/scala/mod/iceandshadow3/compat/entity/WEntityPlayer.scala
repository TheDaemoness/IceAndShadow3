package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.CNVVec3._
import mod.iceandshadow3.compat.TNamed
import mod.iceandshadow3.compat.item.{WInventory, WItemStack}
import mod.iceandshadow3.compat.world.WDimension
import mod.iceandshadow3.spatial.IVec3
import mod.iceandshadow3.util.{E3vl, IteratorConcat}
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

import scala.collection.JavaConverters._

class WEntityPlayer protected[entity](protected[compat] val player: PlayerEntity) extends WEntityLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f
	def deshield(force: Boolean = true): Unit = player.disableShield(force)
	def bed: IVec3 = player.getBedLocation(this.dimensionCoord.dimtype)
	def message(msg: String, actionBar: Boolean, names: TNamed*): Unit = player.sendStatusMessage(
		new TranslationTextComponent(
			s"${IaS3.MODID}.message.$msg",
			names.map(_.getNameTextComponent):_*
		), actionBar
	)
	def message(msg: String): Unit = message(msg, true)

	override def home(where: WDimension): Option[IVec3] =
		Option(player.getBedLocation(where.dimensionCoord.dimtype)).fold(super.home(where)){pos => Option(fromBlockPos(pos))}

	override def isCreative = player.isCreative

	def inventory() = new WInventory(player.inventory)
	def inventoryEnder() = new WInventory(player.getInventoryEnderChest)

	override def items(): Iterator[WItemStack] = {
		val inv = player.inventory
		new IteratorConcat[ItemStack, WItemStack](
			(is: ItemStack) => {new WItemStack(is, player)},
			inv.offHandInventory.iterator,
			inv.armorInventory.iterator,
			inv.mainInventory.iterator
		)
	}
	override def itemsStashed(): Iterator[WItemStack] = inventoryEnder().iterator

	override def saveItem(what: WItemStack): Boolean =
		new WInventory(player.getInventoryEnderChest).add(what)

	def donateToEnderChest(what: WItemStack): E3vl = {
		if(findItem(what, restrictToHands = false).isEmpty) {
			inventoryEnder().donate(what)
		} else E3vl.NEUTRAL
	}

	def advancement(name: String, criteria: String*): Unit = player match {
		case mp: ServerPlayerEntity =>
			val what = mp.getServer.getAdvancementManager.getAdvancement(new ResourceLocation(IaS3.MODID, name))
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

	def give(what: WItemStack) = player.inventory.addItemStackToInventory(what.exposeItems())
}
