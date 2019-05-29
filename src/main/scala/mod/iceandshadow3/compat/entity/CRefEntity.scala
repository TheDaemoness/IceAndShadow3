package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.basics.damage.Damage
import mod.iceandshadow3.compat.Vec3Conversions
import mod.iceandshadow3.compat.dimension.CDimensionCoord
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.TCWorldPlace
import mod.iceandshadow3.forge.{TeleporterDeferred, TeleporterExact}
import mod.iceandshadow3.spatial.{IPositional, IVec3}
import mod.iceandshadow3.util.EmptyIterator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.Teleporter
import net.minecraftforge.common.DimensionManager

class CRefEntity protected[entity](protected[compat] val entity: Entity)
	extends TCWorldPlace
		with TEffectSource
		with IPositional
{
	override def getEffectSourceEntity: Entity = entity
	override def getNameTextComponent: ITextComponent = entity.getDisplayName
	override def position = Vec3Conversions.fromEntity(entity)
	
	override protected[compat] def exposeWorld(): net.minecraft.world.World = entity.world

	override def getAttack: Damage = entity match {
		case source: TEffectSource => source.getAttack
		case _ => null
	}

	def damage(attack: Damage): Unit = 
		entity.attackEntityFrom(new ADamageSource(attack), attack.onDamage(this))

	def teleport(newpos: IVec3): Unit = {
		//TODO: For very long teleports, do we still need to do chunk loading shenanigans ala gatestones?
		val pitchyaw = entity.getPitchYaw
		entity.setPositionAndUpdate(newpos.xDouble, newpos.yDouble, newpos.zDouble)
	}
	def teleport(newpos: IVec3, dim: CDimensionCoord): Unit =
		entity.changeDimension(dim.dimtype, new TeleporterExact(newpos))
	def teleport(dim: BDimension): Unit = {
		if(!dim.isEnabled) {
			IaS3.bug(new NullPointerException, s"Attempted to teleport to a disabled BDimension $dim.")
		} else entity.changeDimension(dim.coord.dimtype, new TeleporterDeferred(dim))
	}
	def teleportVanilla(dim: CDimensionCoord): Unit =
		entity.changeDimension(
			dim.dimtype,
			new Teleporter(DimensionManager.getWorld(
					entity.getServer, dim.dimtype, true, true
				)
			)
		)
	def items(): Iterator[CRefItem] = new EmptyIterator[CRefItem]
	def extinguish(): Unit = entity.extinguish()
}
object CRefEntity {
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
