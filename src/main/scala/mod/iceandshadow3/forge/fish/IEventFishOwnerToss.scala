package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.util.L3

trait IEventFishOwnerToss extends IEventFishOwner {
  /** Called when an item is tossed into the world.
    * @return FALSE to NOT spawn an EntityItem.
    */
  def onOwnerToss(variant: Int, state: BStateData, item: CRefItem): L3
}
