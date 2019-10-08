package mod.iceandshadow3.lib.util.collect

import scala.collection.immutable.ArraySeq

/** A read-only associative map, mapping 3d coordinates to values in O(1) time.
	*/
class FixedMap3d[@specialized(Float) T](
	val xFrom: Int, val yFrom: Int, val zFrom: Int,
	val xWidth: Int, val yWidth: Int, val zWidth: Int,
	compute: (Int, Int, Int) => T
) {
	def this(xWidth: Int, yWidth: Int, zWidth: Int, compute: (Int, Int, Int) => T) =
		this(0, 0, 0, xWidth, yWidth, zWidth, compute)
	val arrays = ArraySeq.untagged.tabulate[T](xWidth*zWidth, yWidth)((xzi, yit) => {
		compute(xzi%xWidth+xFrom, yit+yFrom, xzi/xWidth+zFrom)
	})
	def apply(x: Int, y: Int, z: Int): T = arrays((x-xFrom)+(z-zFrom)*xWidth)(y-yFrom)
}
