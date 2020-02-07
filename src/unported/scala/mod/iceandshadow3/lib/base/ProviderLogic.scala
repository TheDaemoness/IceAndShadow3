package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib._
import mod.iceandshadow3.lib.compat.id.WId
import mod.iceandshadow3.lib.util.TFaceted
import mod.iceandshadow3.multiverse.DomainAlien

import scala.reflect.ClassTag

sealed trait ProviderLogic[LogicType <: LogicCommon] extends TFaceted[Object] with TNamed[WId] {
	@Nullable def getLogic: LogicType
	def getDomain: Domain = {
		val logos = getLogic
		if(logos == null) DomainAlien else logos.domain
	}
	override def facet[What <: Object : ClassTag] = {
		val lp = getLogic
		if(lp == null) None else lp.facet[What]
	}
	override def name = {
		val lp = getLogic
		lp.name
	}
	override def namespace = IaS3.MODID
}
object ProviderLogic {
	trait Block extends ProviderLogic[BLogicBlock]
	trait Item extends ProviderLogic[BLogicItem]
	trait Entity extends ProviderLogic[LogicEntity]
	trait Mob extends ProviderLogic[LogicEntityMob]
}
