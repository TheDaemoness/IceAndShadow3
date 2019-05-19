package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.Vec3Conversions._
import mod.iceandshadow3.util.Vec3
import net.minecraft.entity.player.EntityPlayer

//TODO: Manually generated class stub.
class CRefPlayer(player: EntityPlayer) extends CRefLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f
	def deshield(force: Boolean = true): Unit = player.disableShield(force)
	def bed: Vec3 = player.bedLocation
}
