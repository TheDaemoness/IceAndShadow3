package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.CDimension
import mod.iceandshadow3.spatial.{IVec3, Vec3Mutable}
import mod.iceandshadow3.util.{EmptyIterator, IteratorConcat}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack

class CRefLiving protected[entity](protected[compat] val living: EntityLivingBase) extends CRefEntity(living) {
  def sneaking = living.isSneaking
  def sprinting = living.isSprinting
  def hp: Float = living.getHealth
  def hpTemp: Float = living.getAbsorptionAmount
  def hpReal: Float = hp - hpTemp
  def hpMax: Float = living.getMaxHealth
  def undead: Boolean = living.isEntityUndead
  def heal(amount: Float = hpMax): Unit = living.heal(amount)
  def setHp(amount: Float = hpMax): Unit = living.setHealth(amount)

  def home(where: CDimension): Option[IVec3] = Option(where.getWorldSpawn)

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
  def saveItem(what: CRefItem): Boolean = false

  def isCreative = false

  def visibleTo(who: CRefEntity): Boolean = living.canEntityBeSeen(who.entity)
  def equipment(where: EquipPoint): CRefItem = where.getItem(living)

  def hasIaSArmor: Boolean = false //TODO: IaS3 Armor NYI

  def findItem(itemid: String, restrictToHands: Boolean): CRefItem =
    findItem(CRefItem.make(itemid).changeOwner(this), restrictToHands)
  def findItem(tofind: CRefItem, restrictToHands: Boolean): CRefItem = {
    val tosearch = if(restrictToHands) itemsHeld() else items()
    tosearch.find{tofind.matches}.getOrElse(new CRefItem(null, living))
  }

  def itemsWorn(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getArmorInventoryList.iterator)
  def itemsHeld(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getHeldEquipment.iterator)
  def itemsEquipped(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getEquipmentAndArmor.iterator)
  def itemsStashed(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, new EmptyIterator[ItemStack])
  override def items(): Iterator[CRefItem] = itemsEquipped()
}
