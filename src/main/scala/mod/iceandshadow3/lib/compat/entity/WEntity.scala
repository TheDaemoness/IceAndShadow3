package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.util.{CNVCompat, TLocalized}
import mod.iceandshadow3.lib.compat.world.TWWorldPlace
import mod.iceandshadow3.lib.spatial.{IPositionalFine, IVec3}
import mod.iceandshadow3.lib.util.collect.IteratorEmpty
import mod.iceandshadow3.lib.BParticleType
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.LightType

class WEntity protected[entity](protected[compat] val expose: Entity)
	extends TWWorldPlace
	with TLocalized
	with IPositionalFine {
	def ticks = expose.ticksExisted
	override def posFine = CNVCompat.fromEntity(expose)
	override def getLocalizedName: ITextComponent = expose.getDisplayName
	override protected[compat] def exposeWorld(): net.minecraft.world.World = expose.world

	override def sunlight: Int = exposeWorld().getLightFor(
		LightType.SKY, CNVCompat.toBlockPos(posFine).add(0,1,0)
	)
	def isCreative = false

	/** Returns the entity's eye height. */
	def height: Float = expose.getEyeHeight

	def teleport(newpos: IVec3): Unit = {
		//TODO: For very long teleports, do we still need to do chunk loading shenanigans ala gatestones?
		val pitchyaw = expose.getPitchYaw
		expose.setPositionAndUpdate(newpos.xDouble, newpos.yDouble, newpos.zDouble)
	}

	def impulse(x: Double, y: Double, z: Double): Unit = expose.addVelocity(x, y, z)
	def slow(x: Double, y: Double, z: Double): Unit = {
		val motion = expose.getMotion
		motion.mul(1/(1+x), 1/(1+y), 1/(1+z))
		expose.setMotion(motion)
	}
	def items(): Iterator[WItemStack] = new IteratorEmpty[WItemStack]

	def extinguish(): Unit = expose.extinguish()
	def kill(): Unit = expose.onKillCommand()

	def remove(): Unit = expose.remove()

	def particle(what: BParticleType, vel: IVec3): Unit = super.particle(what, posFine, vel)

	protected[compat] def damageItem(is: ItemStack, amount: Int): Unit = {
		is.setDamage(Math.max(is.getMaxDamage, is.getDamage + amount))
	}
}

