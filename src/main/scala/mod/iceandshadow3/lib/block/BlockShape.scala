package mod.iceandshadow3.lib.block

case class BlockShape(isPole: Boolean, boxes: BlockSubCuboid*)

object BlockShape {
	val FULL_CUBE = BlockShape(false, BlockSubCuboid())
	val EMPTY = BlockShape(isPole = false)
}
