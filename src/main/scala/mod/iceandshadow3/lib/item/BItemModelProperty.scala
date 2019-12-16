package mod.iceandshadow3.lib.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import mod.iceandshadow3.lib.compat.world.TWWorld

abstract class BItemModelProperty(protected val logic: BLogicItem) {
	def name: String
	def valueUnowned(item: WItemStack): Float
	def valueUnowned(item: WItemStack, world: TWWorld): Float = valueUnowned(item)
	def valueOwned(item: WItemStackOwned[WEntityLiving]): Float = valueUnowned(item, item)
}