package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.PairXZ

/** Data for an aligned 32x256x32 world generation cell.
	*/
object BWorldGenRegionTerrain {
	final val width = 32
	final val widthChunk = 16
	def coord(blockCoord: Int): Int = (blockCoord + 16) >> 5
	def toEdge(regionCoord: Int): Int = regionCoord*32 - 16
}
abstract class BWorldGenRegionTerrain(coord: PairXZ) extends BWorldGenRegionSquare (
	BWorldGenRegionTerrain.toEdge(coord.x),
	BWorldGenRegionTerrain.toEdge(coord.z)
) {
	final val width = BWorldGenRegionTerrain.width
}
