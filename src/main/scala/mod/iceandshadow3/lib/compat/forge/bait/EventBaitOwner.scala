package mod.iceandshadow3.lib.compat.forge.bait

import mod.iceandshadow3.lib.LogicItem
import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.compat.forge.fish.TEventFishOwner
import net.minecraftforge.event.entity.living.LivingEvent

import scala.reflect.ClassTag

abstract class EventBaitOwner[Event <: LivingEvent: ClassTag]
  extends EventBait[Event]
{
  type FishType <: TEventFishOwner
  protected def catchFish(item: LogicItem): Option[FishType]
  protected def handleFish(
    event: Event, item: WItemStackOwned[WEntityLiving], logic: LogicItem, fish: FishType
  ): Unit

  override protected def handle(event: Event): Unit = {
    for(item <- CNVEntity.wrap(event.getEntityLiving).items()) {
      val logic = item.getLogic
      if(logic != null) catchFish(logic).foreach(
        handleFish(event, item, logic, _)
      )
    }
  }
}
