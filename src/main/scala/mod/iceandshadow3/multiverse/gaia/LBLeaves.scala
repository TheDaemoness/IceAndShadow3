package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.block.VarBlockOrd
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.compat.block._
import mod.iceandshadow3.lib.compat.item.WItemType
import mod.iceandshadow3.lib.compat.loot.{BLoot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.multiverse.DomainGaia

class LBLeaves(name: String, materia: Materia, val parent: LBLog)
extends LogicBlock(DomainGaia, name, materia) {
	val varSupport = new VarBlockOrd("support", parent.leafSupport)

	override def variables = Array(varSupport)

	override def canStayAt(block: WBlockView, preexisting: Boolean) = {
		preexisting || calcSupport(block) >= 0
		// ^ We avoid doing the support calculations twice for preexisting blocks.
	}

	override def toPlace(state: WBlockState, context: WUsagePlace) = {
		state + (varSupport, calcSupport(context.block, 0))
	}

	override def onNeighborChanged(us: WBlockRef, them: WBlockRef): WBlockState = {
		val newsupport = calcSupport(us)
		if(newsupport < 0) CommonBlockTypes.AIR
		else if(us.get(varSupport).intValue() != newsupport) us + (varSupport, newsupport)
		else us
	}

	def calcSupport(loc: WBlockView, defaultSupport: Int = -1): Int = {
		var support = defaultSupport
		for(adj <- AdjacentBlocks.Surrounding(loc)) {
			if(BlockQueries.hasLogic(parent)(adj)) return parent.leafSupport-1
			else {
				val othersupport = adj(varSupport).getOrElse(0)
				if(othersupport > support) support = othersupport-1
			}
		}
		support
	}

	override def areSurfacesFull = false

	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = what.addOne(
		//TODO: Fortune.
		BLoot.silktouch(this).orElse(BLoot(WItemType("minecraft:flint")).chance(0.125f))
	)
}
