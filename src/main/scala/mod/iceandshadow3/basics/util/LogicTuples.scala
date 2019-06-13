package mod.iceandshadow3.basics.util

import mod.iceandshadow3.compat.BLogic

class LogicPair[Logic <: BLogic](l: Logic, v: Int) extends {
	def logic = l
	def variant = v
}

case class LogicArgs[Logic <: BLogic](variant: Int, state: Logic#StateDataType)

class LogicTriad[Logic <: BLogic](l: Logic, v: Int, s: Logic#StateDataType) extends LogicPair(l, v) {
	def this(lp: LogicPair[Logic], s: Logic#StateDataType) = this(lp.logic, lp.variant, s)
	def state = s
	implicit def toArgs: LogicArgs[Logic] = LogicArgs(v, s)
}

