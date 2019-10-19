package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.compat.item.WItemType

abstract class BLogicWithItem(domain: BDomain, name: String) extends BLogic(domain, name) {
	def stackLimit(variant: Int): Int
	def asWItem(variant: Int): WItemType
	def hasItem(variant: Int): Boolean
}