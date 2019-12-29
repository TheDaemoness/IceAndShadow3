package mod.iceandshadow3.lib.base

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.compat.WId
import mod.iceandshadow3.lib.util.{GeneralUtils, TFaceted}

import scala.reflect.ClassTag

abstract class BLogic(val domain: BDomain, protected val baseName: String)
extends TNamed[WId] with TFaceted[Object] {
	def isTechnical: Boolean = false
	def tier: Int
	def itemLogic: Option[BLogic with TLogicWithItem]

	/** Used for both localization and model directory querying. */
	def pathPrefix: String

	override def toString = s"$name ($pathPrefix)"

	@Nullable
	def nameOverride(stack: WItemStack): String = null

	override def facet[T <: Object: ClassTag]: Option[T] = GeneralUtils.cast[T](this)
}