package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}

abstract class BLogicWithItem(domain: BDomain, baseName: String) extends BLogic(domain, baseName) {
	def stackLimit: Int
	def toWItem: WItemType
	def asWItemStack: WItemStack = toWItem.asWItemStack()
	def hasItem: Boolean
}