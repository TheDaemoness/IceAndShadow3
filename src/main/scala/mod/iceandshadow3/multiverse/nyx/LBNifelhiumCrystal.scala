package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.block.{BlockShape, BlockSubCuboid}
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, Materia, WBlockRef, WBlockState, WBlockView}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.BJsonGenAssetsBlock
import mod.iceandshadow3.multiverse.DomainNyx

object LBNifelhiumCrystal {
	val materia =
		Materia.builder(Materia.ice_blue).hardness(0f).opacity(8)("nifehlium_crystal")
}
class LBNifelhiumCrystal extends LogicBlock(DomainNyx, "nifelhium_crystal", LBNifelhiumCrystal.materia) {
	override def areSurfacesFull = false
	override def harvestXP(what: WBlockView, silktouch: Boolean): Int = 13
	override def canStayAt(block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Below(block).forall(BlockQueries.solid)
	override def isDiscrete = true
	override val shape: BlockShape = BlockShape(true, BlockSubCuboid(10, 0, 13)) //Slightly inside the texture bounds.
	override def onInside(us: WBlockRef, who: WEntity): Unit = {
		us.break(true)
	}
	override def onReplaced(us: WBlockState, them: WBlockRef, moved: Boolean): Unit = {
		//TODO: AoE damage burst that creates icicles.
	}

	override def getGenAssetsBlock = Some(BJsonGenAssetsBlock.deco(this))
}
