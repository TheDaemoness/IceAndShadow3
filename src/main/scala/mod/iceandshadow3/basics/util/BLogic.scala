package mod.iceandshadow3.basics.util

import javax.annotation.Nullable
import mod.iceandshadow3.basics.{BDomain, BStateData}
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.forge.fish.TEventFish

import scala.reflect.{ClassTag, classTag}

abstract class BLogic(protected val domain: BDomain, protected val name: String) {
	type StateDataType <: BStateData

	def countVariants: Int = 1
	def isTechnical: Boolean = false
	def getTier(variant: Int): Int
	def getDomain: BDomain = domain
	def getDefaultStateData(variant: Int): StateDataType

	/** Used for both localization and model directory querying. */
	def getPathPrefix: String

	protected def getVariantName(variant: Int): String = null

	override def toString = s"$getName ($getPathPrefix, $countVariants)"

	final def getName: String = getDomain.name+'_'+getBaseName
	final def getName(variant: Int): String = {
		val varname = getVariantName(variant)
		if(varname == null) getName else getName+'_'+varname
	}
	def resistsExousia(variant: Int): Boolean
	protected def getBaseName: String = name

	def getEventFish[T <: TEventFish: ClassTag](variant: Int): Option[T] = {
		val fishtype = classTag[T].runtimeClass
		if (fishtype.isAssignableFrom(this.getClass)) Some(fishtype.cast(this).asInstanceOf[T]) else None
	}
	@Nullable
	def nameOverride(variant: Int, stack: WItemStack): String = null
}