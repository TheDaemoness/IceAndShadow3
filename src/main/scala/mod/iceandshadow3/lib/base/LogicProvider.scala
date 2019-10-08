package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib._
import mod.iceandshadow3.lib.util.TFaceted
import mod.iceandshadow3.multiverse.DomainAlien

import scala.reflect.ClassTag

sealed trait LogicProvider[LogicType <: BLogic] extends TFaceted[Object] {
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
object LogicProvider {
	trait Block extends LogicProvider[BLogicBlock]
	trait Item extends LogicProvider[BLogicItem]
	trait Entity extends LogicProvider[BLogicEntity]
	trait Mob extends LogicProvider[BLogicEntityMob]
}