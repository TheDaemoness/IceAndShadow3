package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem}
import mod.iceandshadow3.lib.compat.item.{ItemQueries, WItemStack, WItemType}
import mod.iceandshadow3.lib.util.MathUtils

import scala.language.implicitConversions

abstract class BLoot[-Context <: WLootContext] extends (Context => WItemStack) {
	def apply(context: Context): WItemStack
	final def orElse[NewContext <: Context](b: NewContext => WItemStack) = new BLoot[NewContext] {
		override final def apply(context: NewContext): WItemStack = {
			val tried = BLoot.this.apply(context)
			if(tried.isEmpty) b(context) else tried
		}
	}
	final def map[NewContext <: Context](fn: (WItemStack, NewContext) => WItemStack) = new BLoot[NewContext] {
		override def apply(context: NewContext) = {
			val tried = BLoot.this.apply(context)
			if(tried.isEmpty) tried else fn(tried, context)
		}
	}
	final def map(fn: WItemStack => WItemStack): BLoot[Context] = map((is: WItemStack, _) => fn(is))
	final def require[NewContext <: Context](fn: NewContext => Boolean) = new BLoot[NewContext] {
		override def apply(context: NewContext) = if(fn(context)) BLoot.this.apply(context) else WItemStack.empty
	}
	final def filter(fn: WItemStack => Boolean) = new BLoot[Context] {
		override def apply(context: Context) = {
			val result = BLoot.this.apply(context)
			if(fn(result)) result else WItemStack.empty
		}
	}
	final def chance(chance: Float) = require((context: Context) => context.rng().nextFloat() <= chance)
}

object BLoot {
	val empty = new BLoot[WLootContext] {
		override def apply(context: WLootContext) = WItemStack.empty
	}
	def silktouch(what: BLoot[WLootContextBlock]) =
		what.require((context: WLootContextBlock) => ItemQueries.silktouch(context.tool))
	def of(what: => WItemType, count: Float = 1, range: Int = 0) = new BLoot[WLootContext] {
		override def apply(context: WLootContext) = what.asWItemStack().setCount(
			MathUtils.roundRandom(count, context.rng())+context.rng().nextInt(range+1)
		)
	}
	implicit def apply(what: => WItemStack): BLoot[WLootContext] = _ => what
	implicit def apply(what: BLogic with TLogicWithItem): BLoot[WLootContext] = apply(what.toWItemStack)
	implicit def apply(what: WItemType): BLoot[WLootContext] = apply(what.asWItemStack())
}
