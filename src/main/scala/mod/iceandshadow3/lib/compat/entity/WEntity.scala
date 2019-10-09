package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.util.{CNVCompat, TLocalized}
import mod.iceandshadow3.lib.compat.world.TWWorldPlace
import mod.iceandshadow3.lib.spatial.{IPositionalFine, IVec3}
import mod.iceandshadow3.lib.util.collect.IteratorEmpty
import mod.iceandshadow3.lib.ParticleType
import net.minecraft.entity.Entity
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.LightType

class WEntity protected[entity](protected[compat] val entity: Entity)
	extends TWWorldPlace
	with TLocalized
	with IPositionalFine {
	override def sunlight: Int = exposeWorld().getLightFor(LightType.SKY, CNVCompat.toBlockPos(posFine).add(0,1,0))

	override def getLocalizedName: ITextComponent = entity.getDisplayName

	override def posFine = CNVCompat.fromEntity(entity)

	override protected[compat] def exposeWorld(): net.minecraft.world.World = entity.world

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
	def kill(): Unit = entity.onKillCommand()

	def remove(): Unit = entity.remove()

	def particle(what: ParticleType, vel: IVec3): Unit = super.particle(what, posFine, vel)
}

