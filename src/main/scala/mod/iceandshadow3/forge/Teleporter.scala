package mod.iceandshadow3.forge

import mod.iceandshadow3.Multiverse
import mod.iceandshadow3.compat.dimension.WDimensionCoord
import mod.iceandshadow3.compat.entity.CNVEntity
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerTeleport {
	@SubscribeEvent
	def onTeleport(event: EntityTravelToDimensionEvent): Unit = {
		val from = event.getEntity.dimension
		val to = event.getDimension
		val iasfrom = Multiverse.lookup(from)
		if(iasfrom != null) {
			if(!iasfrom.handleEscape(CNVEntity.wrap(event.getEntity), WDimensionCoord(to))) {
				event.setCanceled(true)
				return
			}
		}
		val iasto = Multiverse.lookup(to)
		if(iasto != null) {
			event.setCanceled(true)
		}
	}
}
