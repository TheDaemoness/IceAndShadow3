package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.util.IteratorConcat
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import scala.collection.JavaConverters._

//TODO: Manually generated class stub.
class CRefLiving(living: EntityLivingBase) extends CRefEntity(living) {
  def hp: Float = living.getHealth
  def hpTemp: Float = living.getAbsorptionAmount
  def hpReal: Float = hp - hpTemp
  def hpMax: Float = living.getMaxHealth
  def undead: Boolean = living.isEntityUndead
  def heal(amount: Float = hpMax): Unit = living.heal(amount)
  def setHp(amount: Float = hpMax) = living.setHealth(amount)

  def visibleTo(who: CRefEntity): Boolean = living.canEntityBeSeen(who.entity)
  def equipment(where: EquipPoint): CRefItem = where.getItem(living)

  def hasIaSArmor: Boolean = false //TODO: IaS3 Armor NYI

  def findItem(itemid: String, restrictToHands: Boolean = false): CRefItem = new CRefItem(null, living)

  override def items(): Iterator[CRefItem] =
    new IteratorConcat((is: ItemStack) => {new CRefItem(is, living)}, living.getArmorInventoryList.iterator)
}
