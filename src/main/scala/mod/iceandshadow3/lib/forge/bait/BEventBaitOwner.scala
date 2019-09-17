package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.LogicPair
import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.forge.fish.TEventFishOwner
import net.minecraftforge.event.entity.living.LivingEvent

import scala.reflect.ClassTag

abstract class BEventBaitOwner[Event <: LivingEvent: ClassTag]
  extends BEventBait[Event]
{
  type FishType <: TEventFishOwner
  protected def catchFish(logicpair: LogicPair[BLogicItem]): Option[FishType]
  protected def handleFish(event: Event, item: WItemStack, lp: LogicPair[BLogicItem], fish: FishType): Unit

  override protected def handle(event: Event): Unit = {
    val owner = CNVEntity.wrap(event.getEntityLiving)
    owner.items().foreach(item => {
      val logicpair = item.getLogicPair
      if(logicpair != null) catchFish(logicpair).foreach(fish => {
        handleFish(event, item, logicpair, fish)
      })
    })
  }
}
