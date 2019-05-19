package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.CRefWorld

abstract class BItemProperty {
	def name: String
	def call(item: CRefItem, world: CRefWorld): Float
}