package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.util.E3vl

trait IEventFishOwnerToss extends IEventFishOwner {
  /** Called when an item is tossed into the world.
    * At the time of calling, the item is no longer in its owner's inventory.
    * @return FALSE to NOT spawn an EntityItem.
    */
  def onOwnerToss(variant: Int, state: BStateData, item: WRefItem): E3vl
}
