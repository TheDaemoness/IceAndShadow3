package mod.iceandshadow3.compat.entity

import mod.iceandshadow3.compat.TNamed
import mod.iceandshadow3.damage.Attack
import net.minecraft.entity.Entity

trait TEffectSource extends TNamed {
	protected[compat] def getEffectSourceEntity: Entity = null
	def getAttack: Attack
	def getAttackMultiplier: Float = 1f
	def getMaster: TEffectSource = null
}
