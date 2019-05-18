package mod.iceandshadow3.compat.entity

import net.minecraft.entity.player.EntityPlayer

//TODO: Manually generated class stub.
class CRefPlayer(player: EntityPlayer) extends CRefLiving(player) {
	def isOnCooldown = player.getCooledAttackStrength(0f) < 1.0f;
}
