package mod.iceandshadow3.lib.compat.recipe

import com.google.gson.{JsonArray, JsonObject}
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.WId
import net.minecraft.item.crafting.{IRecipe, Ingredient}

sealed class RecipeFactory(
	protected val metadata: RecipeMetadata,
	builder: Seq[Ingredient] => IRecipe[_],
	inputs: IngredientFactory*
) extends TNamed[WId] {
	final override val id = new WId(IaS3.MODID, metadata.name)
	final protected[compat] def build: IRecipe[_] = builder(inputs.map(_.build))
	protected[compat] def advancement: Option[(String, Option[JsonObject])] = {
		import scala.util.chaining._
		val retval = new JsonObject
		retval.addProperty("parent", "minecraft:recipes/root")
		retval.add("rewards", new JsonObject().tap(_.add("recipes", new JsonArray().tap(
			_.add(id.toString)
		))))
		retval.add("criteria", new JsonObject().tap(criteria => {
			RecipeFactory.makeCriterion(
				criteria, RecipeFactory.criterionIdInventory, "minecraft:inventory_changed",
				_.add("items", new JsonArray().tap(array => {
					for(item <- inputs.map(_.conditionJson)) {
						item.foreach(array.add)
					}
				}))
			)
			RecipeFactory.makeCriterion(
				criteria, RecipeFactory.criterionIdRecipe, "minecraft:recipe_unlocked",
				_.addProperty("recipe", id.toString)
			)
		}))
		retval.add("requirements", new JsonArray().tap(_.add(new JsonArray().tap(array => {
			array.add(RecipeFactory.criterionIdInventory)
			array.add(RecipeFactory.criterionIdRecipe)
		}))))
		Some(id.name, Some(retval))
	}
	def withCustomUnlock(name: String = id.name): RecipeFactory = new RecipeFactory(metadata, builder, inputs:_*) {
		override protected[compat] def advancement = Some(name, None)
	}
	def withNoUnlock(): RecipeFactory = new RecipeFactory(metadata, builder, inputs:_*) {
		override protected[compat] def advancement = None
	}
}
object RecipeFactory {
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
	val criterionIdRecipe = "r"
}
