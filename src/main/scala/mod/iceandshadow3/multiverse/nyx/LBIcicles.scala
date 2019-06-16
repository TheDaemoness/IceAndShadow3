package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.basics.BLogicBlockSimple
import mod.iceandshadow3.basics.block.{BlockShape, BlockSides, BlockSubCuboid}
import mod.iceandshadow3.compat.block.{AdjacentBlocks, WBlockRef, WBlockView}
import mod.iceandshadow3.compat.entity.WEntity
import mod.iceandshadow3.multiverse.DomainNyx

class LBIcicles extends BLogicBlockSimple(DomainNyx, "icicles", MatIcicle) {
	override def areSurfacesFull(variant: Int) = false

	override def harvestXP(variant: Int, what: WBlockView, silktouch: Boolean): Int = if(silktouch) 0 else 2

	override def canBeAt(variant: Int, block: WBlockView, preexisting: Boolean) = {
		new AdjacentBlocks.Above(block).areSidesSolid
	}

	override def isDiscrete = true

	override val shape: BlockShape = BlockShape(BlockSides.UNEVEN, true, BlockSubCuboid(11, 3, 14))

	override def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {
		//TODO: ADS Ice Ceiling 4
		block.break(true)
	}
}

