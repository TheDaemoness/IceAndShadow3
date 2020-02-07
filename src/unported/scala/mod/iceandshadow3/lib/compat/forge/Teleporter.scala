package mod.iceandshadow3.lib.compat.forge

import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.world.impl.AModDimension
import mod.iceandshadow3.lib.compat.world.{WDimensionCoord, WWorld}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent
import net.minecraftforge.eventbus.api.EventPriority

/** Actual handler for teleports to/from IaS3 dimensions.
	* Here's hoping your entities don't override changeDimension.
	*/
object Teleporter {
	def registerSelf(): Unit = {
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, onTeleportFrom)
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, onTeleportTo)
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, onArriveAt)
	}
	def onTeleportFrom(event: EntityTravelToDimensionEvent): Unit = {
		val from = event.getEntity.dimension
		val to = event.getDimension
		val iasfrom = AModDimension.lookup(from.getModType)
		val traveler = event.getEntity
		if(iasfrom != null) {
			if(!iasfrom.onDeparture(CNVEntity.wrap(traveler), WDimensionCoord(to))) {
				event.setCanceled(true)
				return
			} else if(to.isVanilla) {
				//Hack to allow end teleports to work (and overworld teleports to be neater).
				val worldto = traveler.getServer.getWorld(to)
				val spawn = worldto.getSpawnCoordinate
				if(spawn != null) event.getEntity.moveToBlockPosAndAngles(spawn, traveler.rotationYaw, traveler.rotationPitch)
			}
		}
	}
	def onTeleportTo(event: EntityTravelToDimensionEvent): Unit = {
		val to = event.getDimension
		val iasto = AModDimension.lookup(to.getModType)
		if(iasto != null) {
			val traveler = event.getEntity
			event.setCanceled(!iasto.onArrivalPre(
				new WWorld(traveler.getServer.getWorld(to)),
				CNVEntity.wrap(traveler)
			))
		}
	}

	def onArriveAt(event: PlayerChangedDimensionEvent): Unit = {
		val to = event.getTo
		val iasto = AModDimension.lookup(to.getModType)
		if(iasto != null) iasto.onArrivalPost(CNVEntity.wrap(event.getPlayer))
	}
}
