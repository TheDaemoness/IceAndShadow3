package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.BLogicBlockSimple
import mod.iceandshadow3.lib.block.BlockVarOrd
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BMateria, BlockQueries, WBlockRef, WBlockState, WBlockView, WUsagePlace}
import mod.iceandshadow3.multiverse.DomainGaia

class LBLeaves(name: String, materia: BMateria, val parent: LBLog)
extends BLogicBlockSimple(DomainGaia, name, materia) {
	val varSupport = new BlockVarOrd("support", parent.leafSupport)

	override def variables = Array(varSupport)

	override def canStayAt(variant: Int, block: WBlockView, preexisting: Boolean) = {
		preexisting || calcSupport(block) >= 0
		// ^ We avoid doing the support calculations twice for preexisting blocks.
	}

	override def onPlaced(state: WBlockState, context: WUsagePlace) = {
		state + (varSupport, calcSupport(context.block, 0))
	}

	override def onNeighborChanged(variant: Int, us: WBlockRef, them: WBlockRef): WBlockState = {
		val newsupport = calcSupport(us)
		if(newsupport < 0) CommonBlockTypes.AIR
		else if(us.get(varSupport).intValue() != newsupport) us + (varSupport, newsupport)
		else us
	}

	def calcSupport(loc: WBlockView, defaultSupport: Int = -1): Int = {
		var support = defaultSupport
		for(adj <- AdjacentBlocks.Surrounding(loc)) {
			if(adj.isAny(BlockQueries.hasLogic(parent))) return parent.leafSupport-1
			else {
				val othersupport = adj(varSupport).getOrElse(0)
				if(othersupport > support) support = othersupport-1
			}
		}
		support
	}

	override def areSurfacesFull(variant: Int) = false
}
