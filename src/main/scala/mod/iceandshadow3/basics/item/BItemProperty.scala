package mod.iceandshadow3.basics.item

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.compat.world.TWWorld

abstract class BItemProperty(protected val logic: BLogicItem) {
	def name: String
	def call(item: WRefItem, world: TWWorld): Float
}