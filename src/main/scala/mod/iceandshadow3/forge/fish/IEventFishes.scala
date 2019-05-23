package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.compat.BLogic
import mod.iceandshadow3.forge.bait.BEventBait

/** IaS3-friendly class for adding event-handling logic to BLogics.
  */
trait IEventFish {
  type Handler <: BEventBait[_] //Forces the author to have a clear idea of what event triggers this fish.
  type Logic <: BLogic
  def getLogic(): Logic
}
trait IEventFishItem extends IEventFish {
  override type Logic = BLogicItem
}
trait IEventFishOwner extends IEventFish {
  override type Logic = BLogicItem
}

//NOTE: Not using self-type in the above specifically to keep our options open.
//Some logics might want to return an event-handling object instead. Maybe.