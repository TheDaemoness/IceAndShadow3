package mod.iceandshadow3.compat.entity

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}

object EntityConversions {
  implicit def wrap(ent: Entity): CRefEntity = ent match {
    case elb: EntityLivingBase => wrap(elb)
    case _ => new CRefEntity(ent)
  }
  implicit def wrap(ent: EntityLivingBase): CRefLiving = ent match {
    case player: EntityPlayer => wrap(player)
    case _ => new CRefLiving(ent)
  }
  implicit def wrap(ent: EntityPlayer): CRefPlayer = new CRefPlayer(ent)
}
