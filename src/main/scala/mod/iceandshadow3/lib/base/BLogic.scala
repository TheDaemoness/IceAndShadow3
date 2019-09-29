package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.util.{Casting, TFaceted}

import scala.reflect.ClassTag

abstract class BLogic(protected val domain: BDomain, protected val name: String) extends INamed with TFaceted[Object] {
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

	@Nullable
	def nameOverride(variant: Int, stack: WItemStack): String = null

	override final def getNames = Array.tabulate(countVariants)(i => getName(i))
	override def facet[T <: Object: ClassTag]: Option[T] = Casting.cast[T](this)
}