package mod.iceandshadow3.forge

import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.item.container.WContainer
import mod.iceandshadow3.lib.compat.world.WWorld
import mod.iceandshadow3.lib.forge.BEventHandler
import mod.iceandshadow3.multiverse.DimensionNyx
import mod.iceandshadow3.multiverse.dim_nyx.LIFrozen
import net.minecraft.entity.item.ItemEntity
import net.minecraft.util.ActionResultType
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.{PlayerContainerEvent, PlayerInteractEvent}
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerNyx extends BEventHandler {
	//TODO: Many of these could be implemented as BDimension calls and generalized.

	@SubscribeEvent
	def onPlayerExposesContainerToTheElements(oops: PlayerContainerEvent.Open): Unit =
		if (DimensionNyx.coord.worldIs(oops.getEntity)) {
			val container = new WContainer(oops.getContainer)
			val player = CNVEntity.wrap(oops.getPlayer)
			DimensionNyx.freezeItems(container, player)
		}

	@SubscribeEvent
	def onInteract(event: PlayerInteractEvent): Unit = {
		val player = CNVEntity.wrap(event.getPlayer)
		if (DimensionNyx.coord.worldIs(event.getWorld) && !player.isCreative) {
			val what = new WItemStack(event.getItemStack)
			val frozen = LIFrozen.freeze(what, new WWorld(event.getWorld), Some(player))
			if(frozen.isDefined) {
				event.getPlayer.setHeldItem(event.getHand, frozen.get.asItemStack())
				event.setCancellationResult(ActionResultType.FAIL)
				event.setCanceled(true)
			}
		}
	}

	@SubscribeEvent
	def onEntityItemJoin(event: EntityJoinWorldEvent): Unit =
		if (DimensionNyx.coord.worldIs(event.getWorld)) {
			event.getEntity match {
				case ei: ItemEntity =>
					val initial = new WItemStack(ei.getItem)
					val frozen = LIFrozen.freeze(initial, new WWorld(event.getWorld), None)
					frozen.foreach(newitems => {
						if(newitems.isEmpty) event.setCanceled(true)
						else ei.setItem(newitems.asItemStack())
					})
				case _ =>
			}
		}
}
