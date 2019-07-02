package mod.iceandshadow3.lib.compat.util

import mod.iceandshadow3.damage.Attack
import net.minecraft.entity.Entity

trait TEffectSource extends TLocalized {
	protected[compat] def getEffectSourceEntity: Entity = null
	def getAttack: Attack
	def getAttackMultiplier: Float = 1f
	def getMaster: TEffectSource = null
}
