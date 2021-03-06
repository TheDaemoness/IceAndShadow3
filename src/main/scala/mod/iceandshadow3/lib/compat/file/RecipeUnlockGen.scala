package mod.iceandshadow3.lib.compat.file

import com.google.gson.{JsonArray, JsonObject}
import mod.iceandshadow3.lib.compat.id.WId
import mod.iceandshadow3.lib.compat.recipe.{IngredientFactory, NewRecipeMetadata}

abstract class RecipeUnlockGen {
	def name(id: WId): String = id.name
	def apply(name: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*): Option[JsonObject]
}
object RecipeUnlockGen {
	def handwritten(nameOverride: String) = new RecipeUnlockGen {
		override def name(name: WId) = nameOverride
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = None
	}
	def handwritten(): RecipeUnlockGen = new RecipeUnlockGen {
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = None
	}
	def none(): RecipeUnlockGen = new RecipeUnlockGen {
		override def name(name: WId) = ""
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = None
	}
	def standard(canDeduce: Boolean = false): RecipeUnlockGen = new RecipeUnlockGen {
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = {
			import scala.util.chaining._
			val retval = new JsonObject
			retval.addProperty("parent", "minecraft:recipes/root")
			retval.add("rewards", new JsonObject().tap(_.add("recipes", new JsonArray().tap(
				_.add(id.nameFull)
			))))
			retval.add("criteria", new JsonObject().tap(criteria => {
				if(canDeduce) {
					makeCriterion(
						criteria, criterionIdDeduce, "minecraft:inventory_changed",
						_.add("items", new JsonArray().tap(array => {
							array.add(new JsonObject().tap(_.addProperty("item", nrm.result.id.nameFull)))
						}))
					)
				}
				makeCriterion(
					criteria, criterionIdInventory, "minecraft:inventory_changed",
					_.add("items", new JsonArray().tap(array => {
						for(item <- inputs.map(_.conditionJson)) {
							item.foreach(array.add)
						}
					}))
				)
				makeCriterion(
					criteria, criterionIdRecipe, "minecraft:recipe_unlocked",
					_.addProperty("recipe", id.nameFull)
				)
			}))
			retval.add("requirements", new JsonArray().tap(_.add(new JsonArray().tap(array => {
				if(canDeduce) array.add(criterionIdDeduce)
				array.add(criterionIdInventory)
				array.add(criterionIdRecipe)
			}))))
			Some(retval)
		}
	}


	private def makeCriterion(
		criteria: JsonObject, key: String, trigger: String, conditions: JsonObject => Unit
	): Unit = {
		import scala.util.chaining._
		criteria.add(key, new JsonObject().tap(criterion => {
			criterion.addProperty("trigger", trigger)
			criterion.add("conditions", new JsonObject().tap(conditions))
		}))
	}
	val criterionIdInventory = "i"
	val criterionIdDeduce = "o"
	val criterionIdRecipe = "r"
}
