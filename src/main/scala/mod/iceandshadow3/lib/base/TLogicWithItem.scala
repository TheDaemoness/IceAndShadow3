package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.{BDomain, BLogicBlock}
import mod.iceandshadow3.lib.compat.file.{BJsonAssetGen, BJsonAssetGenItem}
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}

trait TLogicWithItem {
	this: BLogic =>
	def stackLimit: Int
	def toWItemType: WItemType
	def toWItemStack: WItemStack
}