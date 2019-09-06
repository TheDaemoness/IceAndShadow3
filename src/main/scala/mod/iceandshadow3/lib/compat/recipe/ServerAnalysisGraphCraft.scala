package mod.iceandshadow3.lib.compat.recipe

import com.google.common.collect.HashMultimap
import mod.iceandshadow3.lib.compat.item.WItem
import mod.iceandshadow3.lib.compat.misc.ServerAnalyzer

import scala.jdk.CollectionConverters._

object ServerAnalysisGraphCraft extends ServerAnalyzer[WItem, java.util.Set[CraftingSummary]](server => {
	val underlying = HashMultimap.create[WItem, CraftingSummary]
	for(recipe <- server.getRecipeManager.getRecipes.asScala) {
		CraftingSummary.apply(recipe).foreach(r => underlying.put(r.output, r))
	}
	underlying.get
})
