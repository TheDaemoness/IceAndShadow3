package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.dimension.WDimension
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.spatial.{IVec3, Vec3Mutable}
import mod.iceandshadow3.util.{IteratorEmpty, IteratorConcat}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack

class WEntityLiving protected[entity](protected[compat] val living: EntityLivingBase) extends WEntity(living) {
  def sneaking = living.isSneaking
  def sprinting = living.isSprinting
  def hp: Float = living.getHealth
  def hpTemp: Float = living.getAbsorptionAmount
  def hpReal: Float = hp - hpTemp
  def hpMax: Float = living.getMaxHealth
  def undead: Boolean = living.isEntityUndead
  def heal(amount: Float = hpMax): Unit = living.heal(amount)
  def setHp(amount: Float = hpMax): Unit = living.setHealth(amount)

  def home(where: WDimension): Option[IVec3] = Option(where.getWorldSpawn)

  def facing: IVec3 = {
    val where = living.getForward
    new Vec3Mutable(
      IVec3.fromDouble(where.x),
      IVec3.fromDouble(where.y).toInt,
      IVec3.fromDouble(where.z)
    )
  }
  def facingH: IVec3 = {
    val where = living.getForward
    new Vec3Mutable(
      IVec3.fromDouble(where.x),
      0,
      IVec3.fromDouble(where.z)
    )
  }

  /** Give an item to a special inventory possessed by an entity.
    * For players, this is usually the ender chest.
    * For mobs, this may be some kind of loot-collection chest.
    */
  def saveItem(what: WRefItem): Boolean = false

  def isCreative = false

  def visibleTo(who: WEntity): Boolean = living.canEntityBeSeen(who.entity)
  def equipment(where: EquipPoint): WRefItem = where.getItem(living)

  def hasIaSArmor: Boolean = false //TODO: IaS3 Armor NYI

  def findItem(itemid: String, restrictToHands: Boolean): WRefItem =
    findItem(WRefItem.make(itemid).changeOwner(this), restrictToHands)
  def findItem(tofind: WRefItem, restrictToHands: Boolean): WRefItem = {
    val tosearch = if(restrictToHands) itemsHeld() else items()
    tosearch.find{tofind.matches}.getOrElse(new WRefItem(null, living))
  }

  def itemsWorn(): Iterator[WRefItem] =
    new IteratorConcat((is: ItemStack) => {new WRefItem(is, living)}, living.getArmorInventoryList.iterator)
  def itemsHeld(): Iterator[WRefItem] =
    new IteratorConcat((is: ItemStack) => {new WRefItem(is, living)}, living.getHeldEquipment.iterator)
  def itemsEquipped(): Iterator[WRefItem] =
    new IteratorConcat((is: ItemStack) => {new WRefItem(is, living)}, living.getEquipmentAndArmor.iterator)
  def itemsStashed(): Iterator[WRefItem] =
    new IteratorConcat((is: ItemStack) => {new WRefItem(is, living)}, new IteratorEmpty[ItemStack])
  override def items(): Iterator[WRefItem] = itemsEquipped()
}
