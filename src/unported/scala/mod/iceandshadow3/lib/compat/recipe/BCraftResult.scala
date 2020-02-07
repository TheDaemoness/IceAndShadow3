package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem}
import mod.iceandshadow3.lib.compat.id.{WId, WIdItem}
import mod.iceandshadow3.lib.compat.item.WItemStack

import scala.language.implicitConversions

sealed abstract class BCraftResult {
	protected[compat] def toItemStack: WItemStack
	protected[compat] def id: WId
}
object BCraftResult {
	implicit def apply(what: LogicCommon with TLogicWithItem): BCraftResult = new BCraftResult {
		override protected[compat] def toItemStack = what.toWItemStack
		override protected[compat] def id = what.id
	}
	implicit def apply(what: WIdItem): BCraftResult = new BCraftResult {
		override protected[compat] def toItemStack = what.getOrThrow.asWItemStack()
		override protected[compat] def id = what
	}
}
