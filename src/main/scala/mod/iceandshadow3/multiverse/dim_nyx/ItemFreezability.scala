package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.compat.item.{BWItem, ItemQueries, WItem}
import mod.iceandshadow3.lib.compat.misc.ServerAnalyzerDerived
import mod.iceandshadow3.lib.compat.recipe.{BCraftGraphAnalysis, CraftingSummary, ECraftingType, ServerAnalysisGraphCraft}

object ItemFreezability
extends ServerAnalyzerDerived[WItem, java.util.Set[CraftingSummary], ItemFreezability](
	ServerAnalysisGraphCraft,
	new BCraftGraphAnalysis[ItemFreezability](_) {
		override protected def defaultValue(input: BWItem) = new ItemFreezability(input)

		/** Called with possibility of an Ingredient */
		override protected def resolveWildcard(options: Iterable[ItemFreezability]) = {
			var freezes = options.nonEmpty
			var hot = freezes
			for (pair <- options) {
				freezes &= pair.freezes
				hot &= pair.hot
			}
			new ItemFreezability(freezes, hot)
		}

		/** Called for the folded inputs */
		override protected def combine(inputs: Iterable[ItemFreezability], output: Int) = {
			var freezes = false
			var hot = false
			for(pair <- inputs) {
				freezes |= pair.freezes
				hot |= pair.hot
			}
			new ItemFreezability(freezes, hot)
		}

		/** Called with the default input and the combined values of all the recipes */
		override protected def resolveFinal(default: ItemFreezability, fromRecipes: Iterable[ItemFreezability]) = {
			lazy val inherited = resolveWildcard(fromRecipes)
			val unmarked = !(default.freezes || default.hot)
			new ItemFreezability(
				default.freezes || (unmarked && inherited.freezes),
				default.hot || (unmarked && inherited.hot)
			)
		}

		override protected def shouldFollow(what: CraftingSummary) =
			!(what.craftType == ECraftingType.COOK_SMELT || what.craftType == ECraftingType.COOK_BLAST)
	}
)
case class ItemFreezability(freezes: Boolean, hot: Boolean) {
	def this(input: BWItem) = this({
			def natfreezes = ItemQueries.ingestable(input) || ItemQueries.compostable(input)
			def isfurnace = input.toBlockState.fold(false)(BlockQueries.isFurnace(_))
			def iscoal = input.hasTag("minecraft:coals")
			input.hasTag("iceandshadow3:freezes") || natfreezes || iscoal || isfurnace
		},
		input.hasTag("iceandshadow3:hot")
	)
}
