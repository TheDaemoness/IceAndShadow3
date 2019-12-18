package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicBlockTechnical
import mod.iceandshadow3.lib.block.BlockShape
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, WBlockView}
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainGaia

class LBMoonstoneDust extends LogicBlockTechnical(DomainGaia, "moonstone_dust", Materias.moonstone_dust) {
	override def canStayAt(block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Below(block).forall(_.isPlain)
	override def shouldHaveLootTable = E3vl.FALSE
	override def shape = BlockShape.EMPTY
	//TODO: Subtle particle effect?
}
