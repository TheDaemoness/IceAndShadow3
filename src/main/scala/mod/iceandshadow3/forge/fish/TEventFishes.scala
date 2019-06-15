package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.basics.util.BLogic

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