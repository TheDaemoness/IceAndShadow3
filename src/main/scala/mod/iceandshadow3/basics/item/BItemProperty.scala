package mod.iceandshadow3.basics.item

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.world.TWWorld

abstract class BItemProperty(protected val logic: BLogicItem) {
	def name: String
	def call(item: WItemStack, world: TWWorld): Float
}