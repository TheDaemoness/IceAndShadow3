package mod.iceandshadow3.forge.fish

import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.util.L3

trait IEventFishOwnerDeath extends IEventFishOwner {
  /** Called before an owner of a certain item dies.
    * The attached item is guaranteed to have an owner.
    * @return FALSE if the owner does NOT die.
    */
  def onOwnerDeath(variant: Int, state: BStateData, item: CRefItem, isCanceled: Boolean): L3
  //TODO: When the alternate damage system works, pass the killing damage here.

  /** As onOwnerDeath, but in response to void damage in specific (which onOwnerDeath does NOT reply to).
    */
  def onOwnerVoided(variant: Int, state: BStateData, item: CRefItem, isCanceled: Boolean): L3 = L3.NULL
}
