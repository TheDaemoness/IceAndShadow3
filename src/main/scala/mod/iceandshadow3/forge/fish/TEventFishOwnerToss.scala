package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.{BLogicItem, BStateData}
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.util.E3vl

trait TEventFishOwnerToss extends TEventFishOwner {
  this: BLogicItem =>
  /** Called when an item is tossed into the world.
    * At the time of calling, the item is no longer in its owner's inventory.
    * @return FALSE to NOT spawn an EntityItem.
    */
  def onOwnerToss(variant: Int, state: BStateData, item: WItemStack): E3vl
}