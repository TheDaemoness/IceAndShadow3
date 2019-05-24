package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.basics.util.{LogicPair, LogicTriad}
import mod.iceandshadow3.compat.entity.{CRefEntity, CRefLiving}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.forge.fish.IEventFishOwner
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.eventbus.api.IEventBus

import scala.reflect.ClassTag

abstract class BEventBaitOwner[Event <: LivingEvent: ClassTag](bus: IEventBus)
  extends BEventBait[Event](bus)
{
  type FishType <: IEventFishOwner
  protected def catchFish(logicpair: LogicPair[BLogicItem]): Option[FishType]
  protected def handleFish(event: Event, item: CRefItem, logicTriad: LogicTriad[BLogicItem], fish: FishType): Unit

  override protected def handle(event: Event): Unit = {
    val owner = CRefEntity.wrap(event.getEntityLiving)
    owner.items().foreach(item => {
      val logicpair = item.getLogicPair
      if(logicpair != null) catchFish(logicpair).foreach(fish => {
        handleFish(event, item, item.toLogicTriad(logicpair), fish)
      })
    })
  }
}
