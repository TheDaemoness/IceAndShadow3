package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.item.WItem
import net.minecraft.item.crafting._

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object CraftingSummary {
	//Little bit naughty to return an Option, but some IRecipes can't be summarized.
	def apply(what: IRecipe[_]): Option[CraftingSummary] = {
		val output = what.getRecipeOutput
		if(output.isEmpty) None
		else Some(CraftingSummary(
			what match {
				case _: ShapelessRecipe => ECraftingType.CRAFT_SHAPELESS
				case _: ShapedRecipe => ECraftingType.CRAFT_SHAPED
				case _: SpecialRecipe => ECraftingType.CRAFT_SPECIAL
				case _: FurnaceRecipe => ECraftingType.COOK_SMELT
				case _: SmokingRecipe => ECraftingType.COOK_SMOKE
				case _: CampfireCookingRecipe => ECraftingType.COOK_SMOKE
				case _: BlastingRecipe => ECraftingType.COOK_BLAST
				case _: StonecuttingRecipe => ECraftingType.STONECUT
				case _ => ECraftingType.UNKNOWN
			},
			WItem(output.getItem),
			output.getCount,
			what.getIngredients.asScala.map(_.getMatchingStacks.map(is => WItem(is.getItem)))
		))
	}
}
case class CraftingSummary(craftType: ECraftingType, output: WItem, count: Int, inputs: Iterable[Iterable[WItem]]) {
	def evaluate[T](eval: WItem => T, resolve: Iterable[T] => T, combine: (Iterable[T], Int) => T): T = {
		combine(inputs.map[T](wc => resolve(wc.map[T](eval))), count)
	}
	/** Returns a collection of all the items that might be needed in this crafting recipe. */
	def inputItems: Set[WItem] = {
		val retval = new mutable.HashSet[WItem]
		for(wildcard <- inputs) for(input <- wildcard) retval += input
		retval.toSet
	}
}
