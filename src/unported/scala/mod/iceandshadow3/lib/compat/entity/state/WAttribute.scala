package mod.iceandshadow3.lib.compat.entity.state

import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.IAttribute

case class WAttribute[+FoundIn <: WEntityLiving](private[entity] val attribute: IAttribute) {
	def name: String = attribute.getName
	def clamp(input: Double) = attribute.clampValue(input)
}
object WAttribute {
	val HEALTH_MAX = WAttribute[WEntityLiving](SharedMonsterAttributes.MAX_HEALTH)
	val SPEED_MOVE = WAttribute[WEntityLiving](SharedMonsterAttributes.MOVEMENT_SPEED)
	val ARMOR_VANILLA = WAttribute[WEntityLiving](SharedMonsterAttributes.ARMOR)
	val ARMOR_VANILLA_TOUGHNESS = WAttribute[WEntityLiving](SharedMonsterAttributes.ARMOR_TOUGHNESS)
}
