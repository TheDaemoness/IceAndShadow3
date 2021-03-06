package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.spatial.IVec3
import net.minecraft.entity.{Entity, IProjectile}

//NOTE: Doesn't cover fireballs.

class WProjectile protected[entity](protected[compat] val missile: Entity with IProjectile) extends WEntity(missile) {
	def aim(where: IVec3, speed: Float, deviation: Float): Unit =
		missile.shoot(where.xDouble, where.yDouble, where.zDouble, speed, deviation)
	def aim(who: WEntity, speed: Float, deviation: Float): Unit =
		aim(who.posFine, speed, deviation) //TODO: Assign targets for IaS homing projectiles.
}
