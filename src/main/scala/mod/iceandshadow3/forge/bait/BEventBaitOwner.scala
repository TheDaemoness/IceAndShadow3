package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.basics.util.{LogicPair, LogicTriad}
import mod.iceandshadow3.compat.entity.CNVEntity
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.forge.fish.TEventFishOwner
import net.minecraftforge.event.entity.living.LivingEvent

import scala.reflect.ClassTag

abstract class BEventBaitOwner[Event <: LivingEvent: ClassTag]
  extends BEventBait[Event]
{
  type FishType <: TEventFishOwner
  protected def catchFish(logicpair: LogicPair[BLogicItem]): Option[FishType]
  protected def handleFish(event: Event, item: WItemStack, logicTriad: LogicTriad[BLogicItem], fish: FishType): Unit

  override protected def handle(event: Event): Unit = {
    val owner = CNVEntity.wrap(event.getEntityLiving)
    owner.items().foreach(item => {
      val logicpair = item.getLogicPair
      if(logicpair != null) catchFish(logicpair).foreach(fish => {
        handleFish(event, item, item.toLogicTriad(logicpair), fish)
      })
    })
  }
}
