package mod.iceandshadow3.lib.compat.entity.state

import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntity}
import mod.iceandshadow3.lib.compat.util.CNVCompat
import net.minecraft.util.DamageSource

case class WDamage(private[compat] val damage: DamageSource, severity: Float) {
	def apply(who: WEntity): Boolean =
		if(who.isServerSide) who.expose.attackEntityFrom(damage, severity) else false
	def getCause: Option[WEntity] = {
		val source = damage.getImmediateSource
		if(source == null) None else Some(CNVEntity.wrap(source))
	}
	def getTrueCause: Option[WEntity] = {
		val source = damage.getTrueSource
		if(source == null) None else Some(CNVEntity.wrap(source))
	}
	def where = CNVCompat.fromVec3d(damage.getDamageLocation)

	def isIndirect = getCause.orNull != getTrueCause.orNull
	def isProjectile = damage.isProjectile
	def isMagic = damage.isMagicDamage
	def isFire = damage.isFireDamage
	def isBlast = damage.isExplosion
	def isVanillaPhysical = !damage.isUnblockable
	def ignoresResistance = !damage.isDamageAbsolute
	def isAbsolute = damage.canHarmInCreative

	def unapply(arg: WDamage): Boolean = this.damage == arg.damage
}
