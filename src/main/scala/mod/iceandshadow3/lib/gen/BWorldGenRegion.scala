package mod.iceandshadow3.lib.gen

import javax.annotation.Nullable
import mod.iceandshadow3.lib.spatial.BWorldRegion

object BWorldGenRegion {
	/** Base class for a collection of methods that transform world gen columns. */
	abstract class BColumnTransformer {
		def first(out: WorldGenColumn): Unit
		def valid(out: WorldGenColumn, y: Int): Boolean = true
		def apply(out: WorldGenColumn, y: Int): Unit
	}
	val BColumnTransformer = new Object {
		val NO_OP = new BColumnTransformer {
			override def first(out: WorldGenColumn): Unit = ()
			override def apply(out: WorldGenColumn, y: Int): Unit = ()
		}
	}
}
abstract class BWorldGenRegion(xFrom: Int, zFrom: Int, xMax: Int, zMax: Int)
extends BWorldRegion(xFrom, zFrom, xMax, zMax) {
	def this(xFrom: Int, zFrom: Int, maxOffset: Int) = this(xFrom, zFrom, xFrom+maxOffset, zFrom+maxOffset)
	val yFrom = 1
	val yEnd = 256

	@Nullable def apply(worldGenColumn: WorldGenColumn): Unit
}
