package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem}
import mod.iceandshadow3.lib.compat.{WId, WIdItem}
import mod.iceandshadow3.lib.compat.item.WItemStack

import scala.language.implicitConversions

sealed abstract class CraftResult {
	protected[compat] def toItemStack: WItemStack
	protected[compat] def id: WId
}
object CraftResult {
	implicit def apply(what: BLogic with TLogicWithItem): CraftResult = new CraftResult {
		override protected[compat] def toItemStack = what.toWItemStack
		override protected[compat] def id = what.id
	}
	implicit def apply(what: WIdItem): CraftResult = new CraftResult {
		override protected[compat] def toItemStack = what.getOrThrow.asWItemStack()
		override protected[compat] def id = what
	}
}
