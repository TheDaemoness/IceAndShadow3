package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.spatial.BWorldRegion

abstract class BWorldGenRegionSquare(xFrom: Int, zFrom: Int) extends BWorldRegion(xFrom, zFrom) {
	val width: Int
	override val xWidth = width
	override val zWidth = width
}
