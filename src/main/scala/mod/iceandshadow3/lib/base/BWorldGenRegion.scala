package mod.iceandshadow3.lib.base

/** Data for a 32x256x32 world generation cell.
	*/
abstract class BWorldGenRegion(val xFrom: Int, val zFrom: Int) {
	final val width = BWorldGen.width;
}
