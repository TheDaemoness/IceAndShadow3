package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.damage.Attack
import mod.iceandshadow3.lib.compat.entity.state.impl.ADamageSource
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.util.{CNVCompat, TEffectSource}
import mod.iceandshadow3.lib.compat.world.TWWorldPlace
import mod.iceandshadow3.lib.spatial.{IPositionalFine, IVec3}
import mod.iceandshadow3.lib.util.collect.IteratorEmpty
import mod.iceandshadow3.lib.ParticleType
import net.minecraft.entity.Entity
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.LightType

class WEntity protected[entity](protected[compat] val entity: Entity)
	extends TWWorldPlace
		with TEffectSource
		with IPositionalFine {
	override def getEffectSourceEntity: Entity = entity
	override def sunlight: Int = exposeWorld().getLightFor(LightType.SKY, CNVCompat.toBlockPos(posFine).add(0,1,0))

	override def getLocalizedName: ITextComponent = entity.getDisplayName

	override def posFine = CNVCompat.fromEntity(entity)

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
		else ADamageSource.attack(how, this, attacker.getAttackMultiplier)
	} else false
	def damage(how: Attack, multiplier: Float = 1f): Boolean = if(isServerSide) {
		ADamageSource.attack(how, this, multiplier)
	} else false
	def remove(): Unit = entity.remove()

	def particle(what: ParticleType, vel: IVec3): Unit = super.particle(what, posFine, vel)
}

