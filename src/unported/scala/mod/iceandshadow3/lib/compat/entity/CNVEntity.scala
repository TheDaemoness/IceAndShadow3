package mod.iceandshadow3.lib.compat.entity

import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.entity.{Entity, IProjectile, LivingEntity}

import scala.language.implicitConversions

object CNVEntity {
	implicit def wrap(ent: Entity): WEntity = ent match {
		case elb: LivingEntity => wrap(elb)
		case missile: Entity with IProjectile => wrapProjectile(missile)
		case item : ItemEntity => wrap(item)
		case _ => new WEntity(ent)
	}
	implicit def wrap(item: ItemEntity): WEntityItem = new WEntityItem(item)
	implicit def wrapProjectile(missile: Entity with IProjectile): WProjectile = new WProjectile(missile)
	implicit def wrap(ent: LivingEntity): WEntityLiving = ent match {
		case player: PlayerEntity => wrap(player)
		case _ => new WEntityLiving(ent)
	}
	implicit def wrap(ent: PlayerEntity): WEntityPlayer = ent match {
		case player: ServerPlayerEntity => wrap(player)
		case _ => new WEntityPlayer(ent)
	}
	implicit def wrap(ent: ServerPlayerEntity): WEntityPlayerFull = new WEntityPlayerFull(ent)
}
