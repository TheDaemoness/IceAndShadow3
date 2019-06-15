package mod.iceandshadow3.compat

import javax.annotation.Nullable
import mod.iceandshadow3.basics._
import mod.iceandshadow3.basics.util.{BLogic, LogicPair, LogicTriad}
import mod.iceandshadow3.world.DomainAlien

sealed trait TLogicProvider[LogicType <: BLogic] {
	@Nullable def getLogicPair: LogicPair[LogicType]
	def getDomain: BDomain = {
		val logos = getLogicPair
		if(logos == null) DomainAlien else logos.logic.getDomain
	}
}

trait ILogicBlockProvider extends TLogicProvider[BLogicBlock]
trait ILogicItemProvider extends TLogicProvider[BLogicItem]
trait ILogicEntityProvider extends TLogicProvider[BLogicCommonEntity]
trait ILogicMobProvider extends TLogicProvider[BLogicMob]

trait TLogicStateProvider[LogicType <: BLogic] {
	this: TLogicProvider[LogicType] =>
	def getLogicTriad: Option[LogicTriad[LogicType]]
	def toLogicTriad(logicPair: LogicPair[LogicType]): LogicTriad[LogicType]
}