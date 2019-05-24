package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.entity.{CRefEntity, CRefPlayer}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.CDimensionCoord
import mod.iceandshadow3.world.DomainNyx
import net.minecraft.init.Blocks
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent
import net.minecraftforge.eventbus.api.{IEventBus, SubscribeEvent}

class EventHandlerWayfinderGiver extends BEventHandler {
	@SubscribeEvent
	def onPlaceChest(placeevent: EntityPlaceEvent): Unit = {
		if(placeevent.getPlacedBlock.getBlock == Blocks.ENDER_CHEST) {
			val who = CRefEntity.wrap(placeevent.getEntity)
			if(who.isClientSide) return
			who match {
				case player: CRefPlayer => {
					val dim = player.dimensionCoord
					val mayGiveEarly = IaS3.getCfgServer.early_wayfinder.get && !player.dimension.canRespawnHere
					if(dim == CDimensionCoord.END || mayGiveEarly) {
						//TODO: DEFINITELY needs feedback.
						player.donateToEnderChest(CRefItem.make(DomainNyx.wayfinder, 0))
					}
				}
				case _ =>
			}
		}
	}
}
