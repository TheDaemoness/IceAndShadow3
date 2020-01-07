package mod.iceandshadow3.lib.compat.forge.fish

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.BLogic

/** IaS3-friendly class for adding event-handling logic to BLogics.
  */
trait TEventFish {
  this: BLogic =>
}
trait TEventFishItem extends TEventFish {
  this: BLogicItem =>
}
trait TEventFishOwner extends TEventFish {
  this: BLogicItem =>
}

//NOTE: Not using self-type in the above specifically to keep our options open.
//Some logics might want to return an event-handling object instead. Maybe.