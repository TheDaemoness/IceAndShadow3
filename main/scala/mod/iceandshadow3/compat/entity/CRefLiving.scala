package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.item.CRefItem
import net.minecraft.entity.EntityLivingBase

//TODO: Manually generated class stub.
class CRefLiving(entity: EntityLivingBase) extends CRefEntity(entity) {
  def hp: Float = entity.getHealth
  def hpTemp: Float = entity.getAbsorptionAmount
  def hpReal: Float = hp - hpTemp
  def hpMax: Float = entity.getMaxHealth
  def undead: Boolean = entity.isEntityUndead

  def visibleTo(who: CRefEntity): Boolean = entity.canEntityBeSeen(who.entity)
  def equipment(where: EquipPoint): CRefItem = where.getItem(entity)
}
