package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.compat.item.{BWItem, ItemQueries, WItem}
import mod.iceandshadow3.lib.compat.misc.ServerAnalyzerDerived
import mod.iceandshadow3.lib.compat.recipe.{BCraftGraphAnalysis, CraftingSummary, ECraftingType, ServerAnalysisGraphCraft}

object ItemFreezability
extends ServerAnalyzerDerived[WItem, java.util.Set[CraftingSummary], ItemFreezability](
	ServerAnalysisGraphCraft,
	new BCraftGraphAnalysis[ItemFreezability](_) {
		override protected def defaultValue(input: BWItem) = {
			def hasTagAntifreeze = input.hasTag("iceandshadow3:antifreeze")
			def hasTagFreezes = input.hasTag("iceandshadow3:freezes")
			def natfreezes = !hasTagAntifreeze && (
				ItemQueries.ingestable(input) ||
					ItemQueries.compostable(input) ||
					input.toBlockState.fold(false)(BlockQueries.isFurnace(_)) ||
					input.hasTag("minecraft:coals")
				)
			val freezes = !input.getDomain.resistsFreezing && (hasTagFreezes || natfreezes)
			val unusual = UnusualFreezeMap.apply(input.registryName).isDefined ||
				(hasTagAntifreeze && hasTagFreezes)
			new ItemFreezability(freezes, unusual)
		}

		/** Called with possibility of an Ingredient */
		override protected def resolveWildcard(options: Iterable[ItemFreezability]) = {
			var freezes = options.nonEmpty
			var unusual = freezes
			for (pair <- options) {
				freezes &&= pair.freezes
				unusual &&= pair.unusual
			}
			new ItemFreezability(freezes, unusual)
		}

		/** Called for the folded inputs */
		override protected def combine(inputs: Iterable[ItemFreezability], output: Int) = {
			var freezes = false
			var ruins = false
			for(pair <- inputs) {
				freezes ||= pair.freezes
				ruins ||= (pair.unusual && pair.freezes)
			}
			new ItemFreezability(freezes, ruins)
		}

		/** Called with the default input and the combined values of all the recipes */
		override protected def resolveFinal(default: ItemFreezability, fromRecipes: Iterable[ItemFreezability]) = {
			val inherited = resolveWildcard(fromRecipes)
			new ItemFreezability(
				default.freezes || inherited.freezes,
				default.unusual || inherited.unusual
			)
		}

		override protected def shouldFollow(what: CraftingSummary) = {
			!(what.output.getDomain.resistsFreezing || what.output.hasTag("iceandshadow3:antifreeze")) &&
			!(what.craftType == ECraftingType.COOK_SMELT || what.craftType == ECraftingType.COOK_BLAST)
		}
	}
)
case class ItemFreezability protected(freezes: Boolean, unusual: Boolean)
