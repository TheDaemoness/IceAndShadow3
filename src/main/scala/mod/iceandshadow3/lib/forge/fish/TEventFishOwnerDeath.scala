package mod.iceandshadow3.lib.forge.fish

import mod.iceandshadow3.lib.{BLogicItem, BStateData}
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.util.E3vl

trait TEventFishOwnerDeath extends TEventFishOwner {
  this: BLogicItem =>
  /** Called before an owner of a certain item dies.
    * The attached item is guaranteed to have an owner.
    * @return FALSE if the owner does NOT die.
    */
  def onOwnerDeath(variant: Int, state: BStateData, item: WItemStack, isCanceled: Boolean): E3vl
  //TODO: When the alternate damage system works, pass the killing damage here.

  /** As onOwnerDeath, but in response to void damage in specific (which onOwnerDeath does NOT reply to).
    */
  def onOwnerVoided(variant: Int, state: BStateData, item: WItemStack, isCanceled: Boolean): E3vl = E3vl.NEUTRAL
}
