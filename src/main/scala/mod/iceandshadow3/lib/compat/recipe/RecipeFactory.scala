package mod.iceandshadow3.lib.compat.recipe

import com.google.gson.JsonObject
import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.WId
import net.minecraft.item.crafting.{IRecipe, Ingredient}

final class RecipeFactory(
	protected val metadata: NewRecipeMetadata,
	builder: Seq[Ingredient] => IRecipe[_],
	recipeUnlockGen: BRecipeUnlockGen = BRecipeUnlockGen.standard(),
	inputs: Seq[IngredientFactory]
) extends TNamed[WId] {
	final override val id = new WId(IaS3.MODID, metadata.name)
	def advancementName: String = recipeUnlockGen.name(id)
	protected[compat] def advancement: Option[(String, Option[JsonObject])] = {
		val recipename = advancementName
		if(recipename.isEmpty) None
		else Some(recipename, recipeUnlockGen(id, metadata, inputs:_*))
	}
	protected[compat] def build: IRecipe[_] = builder(inputs.map(_.build))
}
