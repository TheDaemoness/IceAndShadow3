package mod.iceandshadow3.compat.entity

import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.entity.player.EntityPlayer

import scala.language.implicitConversions

object CNVEntity {
	implicit def wrap(ent: Entity): WEntity = ent match {
		case elb: EntityLivingBase => wrap(elb)
		case _ => new WEntity(ent)
	}
	implicit def wrap(ent: EntityLivingBase): WEntityLiving = ent match {
		case player: EntityPlayer => wrap(player)
		case _ => new WEntityLiving(ent)
	}
	implicit def wrap(ent: EntityPlayer): WEntityPlayer = new WEntityPlayer(ent)
}
