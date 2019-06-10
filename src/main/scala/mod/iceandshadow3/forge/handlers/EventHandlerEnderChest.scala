package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.entity.{CNVEntity, WEntityPlayer}
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.CNVVec3._
import mod.iceandshadow3.compat.dimension.WDimensionCoord
import mod.iceandshadow3.util.E3vl
import mod.iceandshadow3.world.DomainNyx
import net.minecraft.block.Blocks
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerEnderChest extends BEventHandler {
	@SubscribeEvent
	def onPlaceChest(placeevent: EntityPlaceEvent): Unit = {
		if(placeevent.getPlacedBlock.getBlock == Blocks.ENDER_CHEST) {
			val who = CNVEntity.wrap(placeevent.getEntity)
			if(who.isClientSide) return
			who match {
				case player: WEntityPlayer =>
					val dim = player.dimensionCoord
					val mayGiveEarly = IaS3.getCfgServer.early_wayfinder.get && !player.dimension.canRespawnHere
					if(dim == WDimensionCoord.END || mayGiveEarly) {
						if(player.donateToEnderChest(WItemStack.make(DomainNyx.Items.wayfinder, 0)) == E3vl.TRUE) {
							DomainNyx.Sounds.portal_subtle.play(player, placeevent.getBlockSnapshot.getPos, 0.4f, 1f)
							player.advancement("vanilla_wayfinder")
						}
					}
				case _ =>
			}
		}
	}
}
