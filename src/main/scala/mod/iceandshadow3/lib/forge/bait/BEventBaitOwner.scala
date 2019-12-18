package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.forge.fish.TEventFishOwner
import net.minecraftforge.event.entity.living.LivingEvent

import scala.reflect.ClassTag

abstract class BEventBaitOwner[Event <: LivingEvent: ClassTag]
  extends BEventBait[Event]
{
  type FishType <: TEventFishOwner
  protected def catchFish(item: BLogicItem): Option[FishType]
  protected def handleFish(
    event: Event, item: WItemStackOwned[WEntityLiving], logic: BLogicItem, fish: FishType
  ): Unit

  override protected def handle(event: Event): Unit = {
    val owner = CNVEntity.wrap(event.getEntityLiving)
    owner.items().foreach(item => {
      val logic = item.getLogic
      if(logic != null) {
        catchFish(logic).foreach(fish => {
          handleFish(event, item, logic, fish)
        })
      }
    })
  }
}
