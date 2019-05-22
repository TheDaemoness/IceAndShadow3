package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.forge.bait.EventBaitDeath
import mod.iceandshadow3.util.L3

trait IEventFishItemDeath extends IEventFishItem {
  override type Handler = EventBaitDeath
  /** Called before an owner of a certain item dies.
    * The attached item is guaranteed to have an owner.
    * @return True if the owner still dies.
    */
  def onOwnerDeath(variant: Int, state: BStateData, item: CRefItem, isCanceled: Boolean): L3
}
