package mod.iceandshadow3.lib.compat.entity

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.BDimension
import mod.iceandshadow3.lib.compat.item.{WInventory, WItemStack}
import mod.iceandshadow3.lib.compat.util.CNVCompat._
import mod.iceandshadow3.lib.compat.util.TLocalized
import mod.iceandshadow3.lib.compat.world.{WDimension, WDimensionCoord, WWorld}
import mod.iceandshadow3.lib.spatial.{IPosBlock, IVec3}
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.lib.util.collect.IteratorConcat
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent

import scala.jdk.CollectionConverters._

class WEntityPlayer protected[entity](protected[compat] val player: PlayerEntity) extends WEntityLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f
	def deshield(force: Boolean = true): Unit = player.disableShield(force)
	def bed: IVec3 = player.getBedLocation(this.dimensionCoord.dimtype)
	def message(msg: String, actionBar: Boolean, names: TLocalized*): Unit = player.sendStatusMessage(
		new TranslationTextComponent(
			s"${IaS3.MODID}.message.$msg",
			names.map(_.getLocalizedName):_*
		), actionBar
	)
	def message(msg: String): Unit = message(msg, actionBar = true)

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

	def teleport(dim: WDimensionCoord, @Nullable placer: WWorld => IVec3): Unit = player match {
		case spe: ServerPlayerEntity =>
			if(isServerSide) {
				if(!WDimensionCoord.isVoid(dim)) {
					val server = spe.getServer.getWorld(dim.dimtype)
						if(placer != null) {
							val where = placer(new WWorld(server))
							spe.teleport(server, where.xDouble, where.yDouble, where.zDouble, spe.rotationYaw, spe.rotationPitch)
						} else spe.changeDimension(dim.dimtype)
				}
			}
		case _ =>
	}
	def teleport(dim: BDimension): Unit = teleport(dim.coord, dim.defaultPlacer)

	def give(what: WItemStack) = player.inventory.addItemStackToInventory(what.exposeItems())

	def setSpawnPoint(where: IPosBlock, dim: WDimensionCoord): Unit =
		player.setSpawnPoint(where.toBlockPos, true, dim.dimtype)
	def setSpawnPoint(where: IPosBlock): Unit =
		setSpawnPoint(where, dimensionCoord)
}
