package mod.iceandshadow3.basics.block

case class BlockShape(sides: BlockSides, isPole: Boolean, boxes: BlockSubCuboid*)

object BlockShape {
	val FULL_CUBE = BlockShape(BlockSides.FULL, false, BlockSubCuboid())
	val DECO = BlockShape(BlockSides.UNEVEN, false)
}
