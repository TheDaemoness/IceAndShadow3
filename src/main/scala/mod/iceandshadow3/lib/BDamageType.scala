package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.item.WItemStack

abstract class BDamageType {
	/**Called when this damage type affects an item, including armor.*/
	def onDamage(amp: Float, item: WItemStack): Float
	/**Called when this damage type affects a living/undead entity.*/
	def onDamage(amp: Float, victim: WEntityLiving): Float
}
