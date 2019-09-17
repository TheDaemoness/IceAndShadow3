package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib._
import mod.iceandshadow3.multiverse.DomainAlien

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