package mod.iceandshadow3.lib.gen

import javax.annotation.Nullable
import mod.iceandshadow3.lib.spatial.BWorldRegion

abstract class BWorldGenRegion(xFrom: Int, zFrom: Int, xMax: Int, zMax: Int)
extends BWorldRegion(xFrom, zFrom, xMax, zMax) {
	def this(xFrom: Int, zFrom: Int, maxOffset: Int) = this(xFrom, zFrom, xFrom+maxOffset, zFrom+maxOffset)
	val yFrom = 1
	val yEnd = 256

	@Nullable def apply(worldGenColumn: WorldGenColumn): Unit
}
