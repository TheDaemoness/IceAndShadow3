package mod.iceandshadow3.lib.compat.recipe

import mod.iceandshadow3.lib.compat.item.WItemType
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
			WItemType(output.getItem),
			output.getCount,
			what.getIngredients.asScala.map(_.getMatchingStacks.map(is => WItemType(is.getItem)))
		))
	}
}
case class CraftingSummary(craftType: ECraftingType, output: WItemType, count: Int, inputs: Iterable[Iterable[WItemType]]) {
	def evaluate[T](eval: WItemType => T, resolve: Iterable[T] => T, combine: (Iterable[T], Int) => T): T = {
		combine(inputs.map[T](wc => resolve(wc.map[T](eval))), count)
	}
	/** Returns a collection of all the items that might be needed in this crafting recipe. */
	def inputItems: Set[WItemType] = {
		val retval = new mutable.HashSet[WItemType]
		for(wildcard <- inputs) for(input <- wildcard) retval += input
		retval.toSet
	}
}
