package mod.iceandshadow3.basics.util

import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.compat.BLogic

class LogicPair[LogicType <: BLogic](l: LogicType, v: Int) extends {
	def logic = l
	def variant = v
}

class LogicTriad[LogicType <: BLogic](l: LogicType, v: Int, s: BStateData) extends LogicPair(l, v) {
	def this(lp: LogicPair[LogicType], s: BStateData) = this(lp.logic, lp.variant, s)
	def state = s
}
