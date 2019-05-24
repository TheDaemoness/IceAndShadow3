package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.entity.{CRefEntity, CRefPlayer}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.{CDimensionCoord, CWorld}
import mod.iceandshadow3.compat.Vec3Conversions._
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
						val world = new CWorld(placeevent.getBlockSnapshot.getWorld)
						DomainNyx.snd_portal_subtle.play(world, placeevent.getBlockSnapshot.getPos, 0.3f, 1f)
						player.playSound(DomainNyx.snd_portal_subtle, 0.5f, 1f)
						player.donateToEnderChest(CRefItem.make(DomainNyx.li_wayfinder, 0))
					}
				}
				case _ =>
			}
		}
	}
}
