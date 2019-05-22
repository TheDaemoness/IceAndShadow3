package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.util.{EmptyIterator, IteratorConcat, Vec3}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack

class CRefLiving protected[entity](living: EntityLivingBase) extends CRefEntity(living) {
  def hp: Float = living.getHealth
  def hpTemp: Float = living.getAbsorptionAmount
  def hpReal: Float = hp - hpTemp
  def hpMax: Float = living.getMaxHealth
  def undead: Boolean = living.isEntityUndead
  def heal(amount: Float = hpMax): Unit = living.heal(amount)
  def setHp(amount: Float = hpMax) = living.setHealth(amount)

  def home: Option[Vec3] = Option(dimension.getWorldSpawn)

  def isCreative = false

  def visibleTo(who: CRefEntity): Boolean = living.canEntityBeSeen(who.entity)
  def equipment(where: EquipPoint): CRefItem = where.getItem(living)

  def hasIaSArmor: Boolean = false //TODO: IaS3 Armor NYI

  def findItem(itemid: String, restrictToHands: Boolean = false): CRefItem = {
    val tosearch = if(restrictToHands) itemsHeld() else items()
    val tofind = CRefItem.make(itemid, living)
    tosearch.find{tofind.matches}.getOrElse(new CRefItem(null, living))
  }

  def itemsWorn(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getArmorInventoryList.iterator)
  def itemsHeld(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getHeldEquipment.iterator)
  def itemsEquipped(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getEquipmentAndArmor.iterator)
  override def items(): Iterator[CRefItem] = itemsEquipped()
}
