package mod.iceandshadow3.lib.base

/** Data for a 16x256x16 world generation cell, non-cached.
	*/
abstract class BWorldGenChunk(val xFrom: Int, val zFrom: Int) {
	final val width = BWorldGen.widthChunk;
}
