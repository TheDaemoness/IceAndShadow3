package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem}
import mod.iceandshadow3.lib.compat.item.{ItemQueries, WItemStack, WItemType}
import mod.iceandshadow3.lib.util.MathUtils

import scala.language.implicitConversions

abstract class Loot[-Context <: WLootContext] extends (Context => WItemStack) {
	def apply(context: Context): WItemStack
	final def orElse[NewContext <: Context](b: NewContext => WItemStack) = new Loot[NewContext] {
		override final def apply(context: NewContext): WItemStack = {
			val tried = Loot.this.apply(context)
			if(tried.isEmpty) b(context) else tried
		}
	}
	final def map[NewContext <: Context](fn: (WItemStack, NewContext) => WItemStack) = new Loot[NewContext] {
		override def apply(context: NewContext) = {
			val tried = Loot.this.apply(context)
			if(tried.isEmpty) tried else fn(tried, context)
		}
	}
	final def map(fn: WItemStack => WItemStack): Loot[Context] = map((is: WItemStack, _) => fn(is))
	final def require[NewContext <: Context](fn: NewContext => Boolean) = new Loot[NewContext] {
		override def apply(context: NewContext) = if(fn(context)) Loot.this.apply(context) else WItemStack.empty
	}
	final def filter(fn: WItemStack => Boolean) = new Loot[Context] {
		override def apply(context: Context) = {
			val result = Loot.this.apply(context)
			if (fn(result)) result else WItemStack.empty
		}
	}
	final def chance(chance: Float) = require((context: Context) => context.rng().nextFloat() <= chance)
	final def blastDecay[NewContext <: WLootContextBlock with Context] = map[NewContext]((is, context) => {
		val blast = context.blast
		if(blast > 0) {
			val count = is.count / (blast + 1)
			is.setCount(MathUtils.roundRandom(count, context.rng()))
		} else is
	})
}

object Loot {
	val empty = new Loot[WLootContext] {
		override def apply(context: WLootContext) = WItemStack.empty
	}
	def silktouch(what: Loot[WLootContextBlock]) =
		what.require((context: WLootContextBlock) => ItemQueries.silktouch(context.tool))
	def of(what: => WItemType, count: Float = 1, range: Int = 0) = new Loot[WLootContext] {
		override def apply(context: WLootContext) = what.asWItemStack().setCount(
			MathUtils.roundRandom(count, context.rng())+context.rng().nextInt(range+1)
		)
	}
	implicit def apply(what: => WItemStack): Loot[WLootContext] = _ => what
	implicit def apply(what: LogicCommon with TLogicWithItem): Loot[WLootContext] = apply(what.toWItemStack)
	implicit def apply(what: WItemType): Loot[WLootContext] = apply(what.asWItemStack())
}
