package mod.iceandshadow3.lib.compat.forge

import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.world.impl.AModDimension
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerDimension extends BEventHandler {
	@SubscribeEvent
	def onPoorInnocentSoulUpdate(victim: LivingUpdateEvent): Unit = {
		val dim = AModDimension.lookup(victim.getEntityLiving.dimension.getModType)
		if(dim != null) dim.onEntityLivingUpdate(CNVEntity.wrap(victim.getEntityLiving))
	}
}
