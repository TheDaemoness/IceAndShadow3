package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.basics.BLogicBlockSimple
import mod.iceandshadow3.basics.block.{BlockShape, BlockSides, BlockSubCuboid}
import mod.iceandshadow3.compat.block.{AdjacentBlocks, WBlockRef, WBlockView}
import mod.iceandshadow3.compat.entity.WEntity
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.world.DomainNyx

class LBIcicle extends BLogicBlockSimple(DomainNyx, "icicles", MatIcicle) {
	override def areSurfacesFull(variant: Int) = false

	override def harvestXP(variant: Int): Int = 2
	override def harvestOverride(variant: Int, block: WBlockRef, fortune: Int) =
		Array(WItemStack.make(DomainNyx.Items.icicle, 0).changeCount(block.rng(2, 2+fortune)))

	override def canBeAt(block: WBlockView, preexisting: Boolean) = {
		new AdjacentBlocks.Above(block).areSidesSolid
	}

	override val shape: BlockShape = BlockShape(BlockSides.UNEVEN, true, BlockSubCuboid(11, 3, 14))

	override def onInside(block: WBlockRef, who: WEntity): Unit = {
		//TODO: ADS Ice Ceiling 4
		block.break(true)
	}
}
