package mod.iceandshadow3.forge

import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.item.{WContainer, WItemStack}
import mod.iceandshadow3.lib.forge.BEventHandler
import mod.iceandshadow3.multiverse.DimensionNyx
import mod.iceandshadow3.multiverse.dim_nyx.LIFrozen
import net.minecraft.entity.item.ItemEntity
import net.minecraft.util.ActionResultType
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.player.{PlayerContainerEvent, PlayerInteractEvent}
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerNyx extends BEventHandler {
	//TODO: Most of this is stretching The Rule.

	@SubscribeEvent
	def onPlayerExposesContainerToTheElements(oops: PlayerContainerEvent.Open): Unit =
		if (DimensionNyx.coord.worldIs(oops.getEntity)) {
			val container = new WContainer(oops.getContainer)
			val player = CNVEntity.wrap(oops.getEntityPlayer)
			DimensionNyx.freezeItems(container, player)
		}

	@SubscribeEvent
	def onInteract(event: PlayerInteractEvent): Unit =
		if (DimensionNyx.coord.worldIs(event.getWorld)) {
			//Countermeasure against having another way of obtaining a banned item and using it.
			val what = new WItemStack(event.getItemStack, event.getEntityPlayer)
			val frozen = LIFrozen.freeze(what, Some(CNVEntity.wrap(event.getEntityPlayer)))
			if(frozen.isDefined) {
				event.setCancellationResult(ActionResultType.FAIL)
				event.getEntityPlayer.setHeldItem(event.getHand, frozen.get.exposeItems())
			}
		}

	@SubscribeEvent
	def onEntityItemJoin(event: EntityJoinWorldEvent): Unit =
		if (DimensionNyx.coord.worldIs(event.getWorld)) {
			event.getEntity match {
				case ei: ItemEntity =>
					val initial = new WItemStack(ei.getItem, null)
					val frozen = LIFrozen.freeze(initial, None)
					frozen.foreach(newitems => {
						if(newitems.isEmpty) event.setCanceled(true)
						else ei.setItem(newitems.exposeItems())
					})
				case _ =>
			}
		}
}
