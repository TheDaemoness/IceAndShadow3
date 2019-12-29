package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem, TNamed}
import mod.iceandshadow3.lib.compat.{WId, WIdItem}
import mod.iceandshadow3.lib.compat.item.WItemStack

import scala.language.implicitConversions

sealed abstract class BCraftResult {
	protected[compat] def toItemStack: WItemStack
	protected[compat] def id: WId
}
object BCraftResult {
	implicit def apply(what: BLogic with TLogicWithItem): BCraftResult = new BCraftResult {
		override protected[compat] def toItemStack = what.toWItemStack
		override protected[compat] def id = what.id
	}
	implicit def apply(what: TNamed[WIdItem]): BCraftResult = new BCraftResult {
		override protected[compat] def toItemStack = what.id.unapply.get.asWItemStack()
		override protected[compat] def id = what.id
	}
}
