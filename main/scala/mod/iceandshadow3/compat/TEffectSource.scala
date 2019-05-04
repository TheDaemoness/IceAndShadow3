package mod.iceandshadow3.compat

import mod.iceandshadow3.basics.Damage
import net.minecraft.entity.Entity

trait TEffectSource extends TLocalizable {
	protected[compat] def getEffectSourceEntity(): Entity = null
	def getAttack(): Damage
	def getMaster(): TEffectSource = null
}