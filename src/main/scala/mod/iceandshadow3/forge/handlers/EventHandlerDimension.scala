package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.compat.entity.CNVEntity
import mod.iceandshadow3.compat.world.impl.AModDimension
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerDimension extends BEventHandler {
	@SubscribeEvent
	def onPoorInnocentSoulUpdate(victim: LivingUpdateEvent): Unit = {
		val dim = AModDimension.lookup(victim.getEntityLiving.dimension.getModType)
		if(dim != null) dim.onEntityLivingUpdate(CNVEntity.wrap(victim.getEntityLiving))
	}
}
