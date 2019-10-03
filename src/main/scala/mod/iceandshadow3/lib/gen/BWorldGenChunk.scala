package mod.iceandshadow3.lib.gen

/** Data for a 16x256x16 world generation cell, non-cached.
	*/
abstract class BWorldGenChunk(xFrom: Int, zFrom: Int) extends BWorldGenRegionSquare(xFrom, zFrom) {
	final val width = 16
}
