package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem}
import mod.iceandshadow3.lib.compat.item.{ItemQueries, WItemStack, WItemType}
import mod.iceandshadow3.lib.util.MathUtils

import scala.language.implicitConversions

/** An immutable chainable WItemStack provider for loot generation. */
abstract class Loot[-Context <: WLootContext] extends (Context => WItemStack) {
	def apply(context: Context): WItemStack
	/** Return a lootgen that, if this lootgen provides an empty stack, provides a different stack instead. */
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
	/** Return a lootgen that checks the specified predicate before generating loot.
		* If it returns true, returns the loot from this object, else returns an empty stack. */
	final def require[NewContext <: Context](fn: NewContext => Boolean) = new Loot[NewContext] {
		override def apply(context: NewContext) = if(fn(context)) Loot.this.apply(context) else WItemStack.empty
	}
	/** Return a lootgen that checks the specified predicate with the loot generated by this.
		* The predicate may modify the passed-in WItemStack. */
	final def filter(fn: WItemStack => Boolean) = new Loot[Context] {
		override def apply(context: Context) = {
			val result = Loot.this.apply(context)
			if (fn(result)) result else WItemStack.empty
		}
	}
	/** Return a lootgen that has a specified chance of not generating loot. */
	final def chance(chance: Float) = require((context: Context) => context.rng().nextFloat() <= chance)
	/** Return a lootgen that degrades the stack size of the loot generated as a result of an explosion breaking a block.
		* The stronger the explosion, the greater the stack size decrease. */
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
	/** Shorthand for require() call that checks for silk touch on the harvesting tool. */
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