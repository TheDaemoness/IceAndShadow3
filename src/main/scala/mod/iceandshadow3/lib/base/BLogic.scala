package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.forge.fish.TEventFish
import mod.iceandshadow3.lib.BDomain

import scala.reflect.{ClassTag, classTag}

abstract class BLogic(protected val domain: BDomain, protected val name: String) extends INamed {
	def countVariants: Int
	def isTechnical: Boolean = false
	def getTier(variant: Int): Int
	def getDomain: BDomain = domain

	/** Used for both localization and model directory querying. */
	def getPathPrefix: String

	protected def getVariantName(variant: Int): String = null

	override def toString = s"$getName ($getPathPrefix, $countVariants)"

	final def getName: String = getDomain.name+'_'+getBaseName
	final def getName(variant: Int): String = {
		val varname = getVariantName(variant)
		if(varname == null) getName else getName+'_'+varname
	}
	protected def getBaseName: String = name

	def getEventFish[T <: TEventFish: ClassTag](variant: Int): Option[T] = {
		val fishtype = classTag[T].runtimeClass
		if (fishtype.isAssignableFrom(this.getClass)) Some(fishtype.cast(this).asInstanceOf[T]) else None
	}
	@Nullable
	def nameOverride(variant: Int, stack: WItemStack): String = null

	override final def getNames = Array.tabulate(countVariants)(i => getName(i))
}