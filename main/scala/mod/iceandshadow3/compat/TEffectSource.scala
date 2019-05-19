package mod.iceandshadow3.compat

import mod.iceandshadow3.basics.Damage
import net.minecraft.entity.Entity

// TODO: Did the gremlins write this one?

trait TEffectSource extends TLocalizable {
	protected[compat] def getEffectSourceEntity: Entity = null
	def getAttack: Damage
	def getMaster: TEffectSource = null
}