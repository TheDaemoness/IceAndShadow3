package mod.iceandshadow3.util.collect

import mod.iceandshadow3.spatial.PairXZ

import scala.reflect.ClassTag

/** A read-only parallel-computed associative map, mapping 2d coordinates to values in O(1) time.
	*/
class FixedMap2d[T: ClassTag](
	val xFrom: Int, val zFrom: Int,
	val xWidth: Int, val zWidth: Int,
	compute: (Int,Int) => T
) {
	def this(xWidth: Int, zWidth: Int, compute: (Int,Int) => T) = this(0, 0, xWidth, zWidth, compute)
	val values = Array.tabulate[T](xWidth*zWidth)((i: Int) => {
		compute(xFrom+(i%xWidth), zFrom+(i/xWidth))
	})

	def apply(x:Int, z:Int): T = values((x-xFrom)+(z-zFrom)*xWidth)
}
