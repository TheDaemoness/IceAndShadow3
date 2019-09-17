package mod.iceandshadow3.lib.forge.fish

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.lib.BLogicItem

trait TEventFishOwnerToss extends TEventFishOwner {
  this: BLogicItem =>
  /** Called when an item is tossed into the world.
    * At the time of calling, the item is no longer in its owner's inventory.
    * @return FALSE to NOT spawn an EntityItem.
    */
  def onOwnerToss(variant: Int, item: WItemStack): E3vl
}
