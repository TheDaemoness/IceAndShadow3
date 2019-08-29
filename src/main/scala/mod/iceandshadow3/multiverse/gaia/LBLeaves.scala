package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.BLogicBlockSimple
import mod.iceandshadow3.lib.block.BlockVarOrd
import mod.iceandshadow3.lib.compat.block.impl.BMateria
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, WBlockRef, WBlockState, WBlockView, WUsagePlace}
import mod.iceandshadow3.multiverse.DomainGaia

class LBLeaves(name: String, materia: BMateria, val parent: LBLog)
extends BLogicBlockSimple(DomainGaia, name, materia) {
	val varSupport = new BlockVarOrd("support", parent.leafSupport)

	override def variables = Array(varSupport)

	override def canBeAt(variant: Int, block: WBlockView, preexisting: Boolean) = {
		AdjacentBlocks.Surrounding(block).one.isAny(
			BlockQueries.hasLogic(parent),
			BlockQueries.varMatches[Int](varSupport, value => value > 0)
		)
	}

	override def onPlaced(state: WBlockState, context: WUsagePlace) = {
		state + (varSupport, calcSupport(context.block))
	}

	override def onNeighborChanged(variant: Int, us: WBlockRef, them: WBlockRef): Unit = {
		val newsupport = calcSupport(us)
		if(us(varSupport).getOrElse(0) != newsupport) us.change(_ + (varSupport, newsupport))
	}

	def calcSupport(loc: WBlockView): Int = {
		var support = 0
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
