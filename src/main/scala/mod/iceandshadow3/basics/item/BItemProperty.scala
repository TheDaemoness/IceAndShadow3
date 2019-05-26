package mod.iceandshadow3.basics.item

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.TCWorld

abstract class BItemProperty(protected val logic: BLogicItem) {
	def name: String
	def call(item: CRefItem, world: TCWorld): Float
}