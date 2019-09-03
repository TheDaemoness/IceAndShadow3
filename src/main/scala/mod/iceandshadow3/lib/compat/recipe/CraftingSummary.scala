package mod.iceandshadow3.lib.compat.recipe

import com.google.common.collect.ImmutableMultimap
import mod.iceandshadow3.lib.compat.item.WItem
import net.minecraft.item.crafting.IRecipe
import net.minecraft.server.MinecraftServer

import scala.collection.mutable
import scala.jdk.CollectionConverters._

object CraftingSummary {
	//Little bit naughty to return an Option, but some IRecipes can't be summarized.
	def apply(what: IRecipe[_]): Option[CraftingSummary] = {
		val output = what.getRecipeOutput
		if(output.isEmpty) None
		else Some(CraftingSummary(
			WItem(output.getItem),
			output.getCount,
			what.getIngredients.asScala.map(_.getMatchingStacks.map(is => WItem(is.getItem)))
		))
	}
	private[compat] def craftingMap(server: MinecraftServer): ImmutableMultimap[WItem, CraftingSummary] = {
		val builder = ImmutableMultimap.builder[WItem, CraftingSummary]
		for(recipe <- server.getRecipeManager.getRecipes.asScala) {
			apply(recipe).foreach(r => builder.put(r.output, r))
		}
		builder.build()
	}
}
case class CraftingSummary(output: WItem, count: Int, inputs: Iterable[Iterable[WItem]]) {
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
