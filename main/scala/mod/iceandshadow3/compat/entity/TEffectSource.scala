package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.basics.damage.Damage
import mod.iceandshadow3.compat.TNamed
import net.minecraft.entity.Entity

trait TEffectSource extends TNamed {
	protected[compat] def getEffectSourceEntity: Entity = null
	def getAttack: Damage
	def getMaster: TEffectSource = null
}
