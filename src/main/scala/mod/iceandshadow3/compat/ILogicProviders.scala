package mod.iceandshadow3.compat

import javax.annotation.Nullable
import mod.iceandshadow3.basics._
import mod.iceandshadow3.basics.util.LogicPair

sealed trait TLogicProvider[LogicType <: BLogic] {
  @Nullable def getLogicPair: LogicPair[LogicType]
}

trait ILogicBlockProvider extends TLogicProvider[BLogicBlock]
trait ILogicItemProvider extends TLogicProvider[BLogicItem]
