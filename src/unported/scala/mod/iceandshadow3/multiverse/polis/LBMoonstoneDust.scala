package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicBlockTechnical
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockShapes, WBlockView}
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock
import mod.iceandshadow3.multiverse.DomainPolis

class LBMoonstoneDust extends LogicBlockTechnical(DomainPolis, "moonstone_dust", Materias.moonstone_dust) {
	override def canStayAt(block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Below(block).forall(_.isPlain)
	override def shape = BlockShapes.empty

	//TODO: Subtle particle effect?
	override def getGenAssetsBlock = Some(JsonGenAssetsBlock.customSingleModel(this))
}
