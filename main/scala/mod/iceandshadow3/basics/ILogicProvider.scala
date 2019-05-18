package mod.iceandshadow3.basics

import mod.iceandshadow3.compat.BLogic

trait ILogicProvider[LogicType <: BLogic] {
	def getLogic(): LogicType
	def getVariant(): Int
}