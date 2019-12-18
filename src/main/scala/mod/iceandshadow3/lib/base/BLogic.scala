package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.util.{Casting, TFaceted}

import scala.reflect.ClassTag

abstract class BLogic(val domain: BDomain, protected val baseName: String)
extends INamed with TFaceted[Object] {
	def isTechnical: Boolean = false
	def tier: Int

	/** Used for both localization and model directory querying. */
	def pathPrefix: String

	override def toString = s"$name ($pathPrefix)"

	final override def name: String = domain.name+'_'+baseName

	@Nullable
	def nameOverride(stack: WItemStack): String = null

	override def facet[T <: Object: ClassTag]: Option[T] = Casting.cast[T](this)
}