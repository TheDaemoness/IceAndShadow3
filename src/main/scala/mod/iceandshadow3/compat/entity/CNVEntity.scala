package mod.iceandshadow3.compat.entity

import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.{Entity, IProjectile, LivingEntity}

import scala.language.implicitConversions

object CNVEntity {
	implicit def wrap(ent: Entity): WEntity = ent match {
		case elb: LivingEntity => wrap(elb)
		case missile: Entity with IProjectile => wrapProjectile(missile)
		case item : ItemEntity => wrap(item)
		case _ => new WEntity(ent)
	}
	implicit def wrap(item: ItemEntity) = new WEntityItem(item)
	implicit def wrapProjectile(missile: Entity with IProjectile) = new WProjectile(missile)
	implicit def wrap(ent: LivingEntity): WEntityLiving = ent match {
		case player: PlayerEntity => wrap(player)
		case _ => new WEntityLiving(ent)
	}
	implicit def wrap(ent: PlayerEntity) = new WEntityPlayer(ent)
}
