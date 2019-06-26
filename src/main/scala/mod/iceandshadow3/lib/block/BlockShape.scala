package mod.iceandshadow3.lib.block

case class BlockShape(sides: BlockSides, isPole: Boolean, boxes: BlockSubCuboid*)

object BlockShape {
	val FULL_CUBE = BlockShape(BlockSides.FULL, false, BlockSubCuboid())
	val EMPTY = BlockShape(BlockSides.UNEVEN, false)
}
