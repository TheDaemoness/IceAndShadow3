package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.IaS3
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipe, Ingredient}
import net.minecraft.util.ResourceLocation

object RecipeBuilderCooking {
	type RecipeMaker = (ResourceLocation, String, Ingredient, ItemStack, Float, Int) => IRecipe[_]
}
class RecipeBuilderCooking(
	ect: ECraftingType, craftResult: => BCraftResult,
	defaultTicks: Int, input: IngredientFactory,
	recipeFn: RecipeBuilderCooking.RecipeMaker
) extends RecipeBuilder(ect, craftResult) {
	protected var cookticks = defaultTicks
	protected var experience = 0f
	def ticks(time: Int): this.type = {
		cookticks = time
		this
	}
	def xp(amount: Float): this.type = {
		experience = amount
		this
	}
	final override protected def factory(nrm: NewRecipeMetadata) = new RecipeFactory(
		nrm,
		inputs => recipeFn(
			IaS3.rloc(nrm.name), nrm.group, inputs.head, nrm.result.asItemStack(), experience, cookticks
		),
		unlock,
		Seq(input)
	)
}
