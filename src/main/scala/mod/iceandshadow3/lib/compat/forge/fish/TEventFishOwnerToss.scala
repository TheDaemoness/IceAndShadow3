package mod.iceandshadow3.lib.compat.forge.fish

import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.lib.LogicItem
import mod.iceandshadow3.lib.compat.entity.WEntityPlayer

trait TEventFishOwnerToss extends TEventFishOwner {
  this: LogicItem =>
  /** Called when an item is tossed into the world.
    * At the time of calling, the item is no longer in its owner's inventory.
    * @return FALSE to NOT spawn an EntityItem.
    */
  def onOwnerToss(item: WItemStackOwned[WEntityPlayer]): E3vl
}
