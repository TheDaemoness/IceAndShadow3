package mod.iceandshadow3.lib.compat.recipe

import com.google.gson.JsonObject
import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem}
import mod.iceandshadow3.lib.compat.id.WIdItem
import mod.iceandshadow3.lib.compat.item.WItemStack
import net.minecraft.item.crafting.Ingredient

import scala.language.implicitConversions

sealed abstract class IngredientFactory { //No relation to BuildCraft.
	protected[compat] def build: Ingredient
	def conditionJson: Option[JsonObject]
	def excludedFromUnlock: IngredientFactory = new IngredientFactory {
		override protected[compat] def build = IngredientFactory.this.build
		override def conditionJson = None
		override def toResult = IngredientFactory.this.toResult
	}
	def toResult: BCraftResult
}
object IngredientFactory {
	import scala.util.chaining._

	val empty: IngredientFactory = new IngredientFactory {
		override protected[compat] def build = Ingredient.EMPTY
		override def conditionJson = None
		override def toResult =
			throw new IllegalStateException("Attempted to get crafting result from empty IngredientFactory")
	}
	implicit def apply(what: LogicCommon with TLogicWithItem): IngredientFactory = new IngredientFactory {
		override protected[compat] def build = Ingredient.fromItems(what.toWItemType.asItem)
		override def conditionJson =
			Some(new JsonObject().tap(_.addProperty("item", what.id.nameFull)))
		override def toResult = what
	}
	implicit def apply(what: WIdItem): IngredientFactory = new IngredientFactory {
		override protected[compat] def build = Ingredient.fromItems(what.getOrThrow)
		override def conditionJson =
			Some(new JsonObject().tap(_.addProperty("item", what.id.nameFull)))
		override def toResult = what
	}
	implicit def apply(what: => WItemStack): IngredientFactory = new IngredientFactory {
		override protected[compat] def build = Ingredient.fromStacks(what.expose())
		//TODO: Consider NBT and all that.
		override def conditionJson =
			Some(new JsonObject().tap(_.addProperty("item", what.asItem().getRegistryName.toString)))
		override def toResult = what.id
	}
}
