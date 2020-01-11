package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.{Domain, BLogicBlock}
import mod.iceandshadow3.lib.compat.file.{JsonGen, JsonGenModelItem}
import mod.iceandshadow3.lib.compat.id.WIdItem
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemType}

trait TLogicWithItem {
	this: LogicCommon =>
	def stackLimit: Int
	def toWItemType: WItemType
	def toWItemStack: WItemStack
}