package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.spatial.PairXZ

/** Data for a 32x256x32 world generation cell.
	*/
abstract class BWorldGenRegion(coord: PairXZ) {
	val xFrom = BWorldGen.toEdge(coord.x)
	val zFrom = BWorldGen.toEdge(coord.z)
	final val width = BWorldGen.widthRegion
}
