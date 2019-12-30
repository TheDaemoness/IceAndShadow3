package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.WId
import com.google.gson.{JsonArray, JsonObject}

abstract class BRecipeUnlockGen {
	def apply(name: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*): Option[(String, Option[JsonObject])]
}
object BRecipeUnlockGen {
	def handwritten(nameOverride: String) = new BRecipeUnlockGen {
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = Some(nameOverride, None)
	}
	def handwritten(): BRecipeUnlockGen = new BRecipeUnlockGen {
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = Some(id.name, None)
	}
	def none(): BRecipeUnlockGen = new BRecipeUnlockGen {
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = None
	}
	def standard(canDeduce: Boolean = false): BRecipeUnlockGen = new BRecipeUnlockGen {
		override def apply(id: WId, nrm: NewRecipeMetadata, inputs: IngredientFactory*) = {
			import scala.util.chaining._
			val retval = new JsonObject
			retval.addProperty("parent", "minecraft:recipes/root")
			retval.add("rewards", new JsonObject().tap(_.add("recipes", new JsonArray().tap(
				_.add(id.toString)
			))))
			retval.add("criteria", new JsonObject().tap(criteria => {
				if(canDeduce) {
					makeCriterion(
						criteria, criterionIdDeduce, "minecraft:inventory_changed",
						_.add("items", new JsonArray().tap(array => {
							array.add(new JsonObject().tap(_.addProperty("item", nrm.result.id.toString)))
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
					_.addProperty("recipe", id.toString)
				)
			}))
			retval.add("requirements", new JsonArray().tap(_.add(new JsonArray().tap(array => {
				if(canDeduce) array.add(criterionIdDeduce)
				array.add(criterionIdInventory)
				array.add(criterionIdRecipe)
			}))))
			Some(id.name, Some(retval))
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
