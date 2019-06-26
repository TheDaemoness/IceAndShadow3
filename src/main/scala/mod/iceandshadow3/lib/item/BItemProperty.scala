package mod.iceandshadow3.lib.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.world.TWWorld

abstract class BItemProperty(protected val logic: BLogicItem) {
	def name: String
	def call(item: WItemStack, world: TWWorld): Float
}