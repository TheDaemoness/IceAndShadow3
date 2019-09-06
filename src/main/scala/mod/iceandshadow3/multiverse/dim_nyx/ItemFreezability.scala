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
			var antifreeze = freezes
			for (pair <- options) {
				freezes &&= pair.freezes
				antifreeze &&= pair.antifreeze
			}
			new ItemFreezability(freezes, antifreeze)
		}

		/** Called for the folded inputs */
		override protected def combine(inputs: Iterable[ItemFreezability], output: Int) = {
			var freezes = false
			var antifreeze = false
			var antifreezeAll = true
			for(pair <- inputs) {
				freezes ||= pair.freezes
				antifreeze ||= pair.antifreeze
				antifreezeAll &&= pair.antifreeze
			}
			new ItemFreezability(freezes, antifreeze && (freezes || antifreezeAll))
		}

		/** Called with the default input and the combined values of all the recipes */
		override protected def resolveFinal(default: ItemFreezability, fromRecipes: Iterable[ItemFreezability]) = {
			lazy val inherited = resolveWildcard(fromRecipes)
			new ItemFreezability(
				default.freezes || inherited.freezes,
				default.antifreeze || inherited.antifreeze
			)
		}

		override protected def shouldFollow(what: CraftingSummary) = {
			!(what.output.getDomain.resistsFreezing) &&
			!(what.craftType == ECraftingType.COOK_SMELT || what.craftType == ECraftingType.COOK_BLAST)
		}
	}
)
case class ItemFreezability(freezes: Boolean, antifreeze: Boolean) {
	def this(input: BWItem) = this({
			def natfreezes = !input.hasTag("iceandshadow3:antifreeze") && (
					ItemQueries.ingestable(input) ||
					ItemQueries.compostable(input) ||
					input.toBlockState.fold(false)(BlockQueries.isFurnace(_)) ||
					input.hasTag("minecraft:coals")
				)
			!input.getDomain.resistsFreezing && (input.hasTag("iceandshadow3:freezes") || natfreezes)
		},
		input.hasTag("iceandshadow3:antifreeze")
	)
}
