package mod.iceandshadow3.lib.compat.forge

import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.entity.state.WDamage
import mod.iceandshadow3.lib.compat.entity.state.impl.AStatusEffect
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

import scala.jdk.CollectionConverters._

class EventHandlerStatus extends EventHandler {
	@SubscribeEvent
	def onHarm(lde: LivingDamageEvent): Unit = {
		val who = CNVEntity.wrap(lde.getEntityLiving)
		val dmgWrapped = WDamage(lde.getSource, lde.getAmount)
		for(ei <- lde.getEntityLiving.getActivePotionEffects.asScala) ei.getPotion match {
			case ase: AStatusEffect =>
				val result = ase.onHarm(who, dmgWrapped, ei.getAmplifier)
				if(result != dmgWrapped.severity) {
					if(result <= 0f) {
						lde.setCanceled(true)
						if(result < 0f) who.heal(-result)
					} else lde.setAmount(result)
				}
			case _ =>
		}
	}
}
