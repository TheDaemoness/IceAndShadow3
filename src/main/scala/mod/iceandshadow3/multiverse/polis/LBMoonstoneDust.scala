package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicBlockTechnical
import mod.iceandshadow3.lib.block.BlockShape
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, WBlockView}
import mod.iceandshadow3.lib.compat.file.BJsonGenAssetsBlock
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainPolis

class LBMoonstoneDust extends LogicBlockTechnical(DomainPolis, "moonstone_dust", Materias.moonstone_dust) {
	override def canStayAt(block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Below(block).forall(_.isPlain)
	override def shape = BlockShape.EMPTY

	//TODO: Subtle particle effect?
	override def getGenAssetsBlock = Some(BJsonGenAssetsBlock.customSingleModel(this))
}
