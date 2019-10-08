package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib._
import mod.iceandshadow3.lib.util.TFaceted
import mod.iceandshadow3.multiverse.DomainAlien

import scala.reflect.ClassTag

sealed trait TLogicProvider[LogicType <: BLogic] extends TFaceted[Object] {
	@Nullable def getLogicPair: LogicPair[LogicType]
	def getDomain: BDomain = {
		val logos = getLogicPair
		if(logos == null) DomainAlien else logos.logic.getDomain
	}
	override def facet[What <: Object : ClassTag] = {
		val lp = getLogicPair
		if(lp == null) None else lp.logic.facet[What]
	}
}

trait ILogicBlockProvider extends TLogicProvider[BLogicBlock]
trait ILogicItemProvider extends TLogicProvider[BLogicItem]
trait ILogicEntityProvider extends TLogicProvider[BLogicEntity]
trait ILogicMobProvider extends TLogicProvider[BLogicEntityMob]