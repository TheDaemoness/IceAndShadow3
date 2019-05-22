package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.compat.entity.{CRefLiving, EntityConversions}
import mod.iceandshadow3.forge.fish.IEventFishItemDeath
import mod.iceandshadow3.util.L3
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.IEventBus

class EventBaitDeath(bus: IEventBus) extends BEventBait[LivingDeathEvent](bus) {
  override protected def handle(event: LivingDeathEvent): Unit = {
    val owner: CRefLiving = EntityConversions.wrap(event.getEntityLiving)
    owner.items().foreach(item => {
      val logicpair = item.getLogicPair
      if(logicpair != null) {
        logicpair.logic.getEventFish[IEventFishItemDeath](logicpair.variant).foreach(fish => {
          val state = item.exposeStateData(logicpair)
          val cancel = fish.onOwnerDeath(logicpair.variant, state, item, event.isCanceled)
          if (cancel != L3.NULL) event.setCanceled(cancel == L3.FALSE)
          item.saveStateData(state)
        })
      }
    })
  }
}