package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.entity.{WEntity, WEntityLiving}
import mod.iceandshadow3.compat.item.WRefItem

abstract class BDamageType {
	/**Called when this damage type affects an item, including armor.*/
	def onDamage(amp: Float, item: WRefItem): Float
	/**Called when this damage type affects a living/undead entity.*/
	def onDamage(amp: Float, victim: WEntityLiving): Float
}
