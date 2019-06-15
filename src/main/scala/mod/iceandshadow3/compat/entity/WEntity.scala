package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BDimension
import mod.iceandshadow3.compat.CNVVec3
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.world.{TWWorldPlace, WDimensionCoord}
import mod.iceandshadow3.damage.Attack
import mod.iceandshadow3.spatial.{IPositional, IVec3}
import mod.iceandshadow3.util.IteratorEmpty
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.LightType

class WEntity protected[entity](protected[compat] val entity: Entity)
	extends TWWorldPlace
		with TEffectSource
		with IPositional {
	override def getEffectSourceEntity: Entity = entity
	override def sunlight: Int = exposeWorld().getLightFor(LightType.SKY, CNVVec3.toBlockPos(position).add(0,1,0))

	override def getNameTextComponent: ITextComponent = entity.getDisplayName

	override def position = CNVVec3.fromEntity(entity)

	override protected[compat] def exposeWorld(): net.minecraft.world.World = entity.world

	override def getAttack: Attack = entity match {
		case source: TEffectSource => source.getAttack
		case _ => null
	}

	def teleport(newpos: IVec3): Unit = {
		//TODO: For very long teleports, do we still need to do chunk loading shenanigans ala gatestones?
		val pitchyaw = entity.getPitchYaw
		entity.setPositionAndUpdate(newpos.xDouble, newpos.yDouble, newpos.zDouble)
	}

	def teleport(dim: WDimensionCoord): Unit = entity.changeDimension(dim.dimtype)

	def teleport(dim: BDimension): Unit = {
		if (!dim.isEnabled) {
			IaS3.bug(new NullPointerException, s"Attempted to teleport to a disabled BDimension $dim.")
		} else teleport(dim.coord)
	}

	def impulse(x: Double, y: Double, z: Double): Unit = entity.addVelocity(x, y, z)
	def slow(x: Double, y: Double, z: Double): Unit = {
		val motion = entity.getMotion
		motion.mul(1/(1+x), 1/(1+y), 1/(1+z))
		entity.setMotion(motion)
	}
	def items(): Iterator[WItemStack] = new IteratorEmpty[WItemStack]

	def extinguish(): Unit = entity.extinguish()

	def damage(attacker: TEffectSource): Boolean = if(isServerSide) {
		val how = attacker.getAttack
		if(how == null) false
		else entity.attackEntityFrom(new ADamageSource(how, attacker), how.baseDamage(attacker.getAttackMultiplier))
	} else false
	def damage(how: Attack, multiplier: Float = 1f): Boolean = if(isServerSide) {
		entity.attackEntityFrom(new ADamageSource(how), how.baseDamage(multiplier)
		)
	} else false
	def remove(): Unit = entity.remove()
}

