package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.lib.LogicBlockSimple
import mod.iceandshadow3.lib.block.{BlockShape, BlockSubCuboid}
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, WBlockRef, WBlockView}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.multiverse.DomainNyx

class LBNifelhiumCrystal extends LogicBlockSimple(DomainNyx, "nifelhium_crystal", MatIcicle) {
	override def areSurfacesFull(variant: Int) = false
	override def harvestXP(variant: Int, what: WBlockView, silktouch: Boolean): Int = 13
	override def canStayAt(variant: Int, block: WBlockView, preexisting: Boolean) =
		AdjacentBlocks.Below(block).forall(BlockQueries.solid)
	override def isDiscrete = true
	override val shape: BlockShape = BlockShape(true, BlockSubCuboid(10, 0, 13)) //Slightly inside the texture bounds.
	override def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {
		block.break(true)
	}
	override def onReplaced(variant: Int, us: WBlockRef, them: WBlockRef, moved: Boolean): Unit = {
		//TODO: AoE damage burst that creates icicles.
	}

	override def getBlockModelGen(variant: Int) = Some(BJsonAssetGen.blockDeco)
}
