package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.entity.{CRefEntity, CRefPlayer}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.{CDimensionCoord, CWorld}
import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.util.L3
import mod.iceandshadow3.world.DomainNyx
import net.minecraft.init.Blocks
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

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
						if(player.donateToEnderChest(CRefItem.make(DomainNyx.li_wayfinder, 0)) == L3.TRUE) {
							DomainNyx.snd_portal_subtle.play(player, placeevent.getBlockSnapshot.getPos, 0.4f, 1f)
							player.advancement("iceandshadow3:wayfinder")
						}
					}
				}
				case _ =>
			}
		}
	}
}
