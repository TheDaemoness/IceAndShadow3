package mod.iceandshadow3.lib.compat.forge.fish

import mod.iceandshadow3.lib.LogicItem
import mod.iceandshadow3.lib.base.LogicCommon

/** IaS3-friendly class for adding event-handling logic to BLogics.
  */
trait TEventFish {
  this: LogicCommon =>
}
trait TEventFishItem extends TEventFish {
  this: LogicItem =>
}
trait TEventFishOwner extends TEventFish {
  this: LogicItem =>
}

//NOTE: Not using self-type in the above specifically to keep our options open.
//Some logics might want to return an event-handling object instead. Maybe.