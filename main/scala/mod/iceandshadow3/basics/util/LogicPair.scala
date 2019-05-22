package mod.iceandshadow3.basics.util

import mod.iceandshadow3.compat.BLogic

case class LogicPair[LogicType <: BLogic](logic: LogicType, variant: Int)
