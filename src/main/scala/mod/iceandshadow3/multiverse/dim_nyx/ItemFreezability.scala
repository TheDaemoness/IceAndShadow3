package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.WIdTagItem
import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.compat.item.{BWItem, ItemQueries, WItemType}
import mod.iceandshadow3.lib.compat.misc.ServerAnalyzerDerived
import mod.iceandshadow3.lib.compat.recipe.{BCraftGraphAnalysis, CraftingSummary, ECraftingType, ServerAnalysisGraphCraft}
import mod.iceandshadow3.lib.util.Is

object ItemFreezability
extends ServerAnalyzerDerived[WItemType, java.util.Set[CraftingSummary], ItemFreezability](
	ServerAnalysisGraphCraft,
	new BCraftGraphAnalysis[ItemFreezability, BWItem => Boolean](_, Is.any[BWItem](
		//Begin conditions for natural freezing.
		ItemQueries.ingestable,
		ItemQueries.compostable,
		_.toBlockState.fold(false)(BlockQueries.isFurnace(_)),
		WIdTagItem("minecraft:coals").unapply
		//End conditions for natural freezing.
	)) {
		import LIFrozen.tagAntifreeze
		import LIFrozen.tagFreezes

		override protected def defaultValue(input: BWItem) = {
			val hasTagAntifreeze = tagAntifreeze.unapply(input)
			val hasTagFreezes = tagFreezes.unapply(input)
			val freezes = !input.getDomain.resistsFreezing && (
				hasTagFreezes ||
				(!hasTagAntifreeze && arg(input))
			)
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
			!(what.output.getDomain.resistsFreezing || tagAntifreeze.unapply(what.output)) &&
			!(what.craftType == ECraftingType.COOK_SMELT || what.craftType == ECraftingType.COOK_BLAST)
		}
	}
)
case class ItemFreezability protected(freezes: Boolean, unusual: Boolean)
