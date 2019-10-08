package mod.iceandshadow3.lib.util.collect

import scala.reflect.ClassTag

/** A read-only associative map, mapping 3d coordinates to values in O(1) time.
	*/
class FixedMap3d[@specialized(Float) T: ClassTag](
	val xFrom: Int, val yFrom: Int, val zFrom: Int,
	val xWidth: Int, val yWidth: Int, val zWidth: Int,
	compute: (Int, Int, Int) => T
) {
	def this(xWidth: Int, yWidth: Int, zWidth: Int, compute: (Int, Int, Int) => T) =
		this(0, 0, 0, xWidth, yWidth, zWidth, compute)
	val values: Array[Array[T]] = {
		val retval = new Array[Array[T]](xWidth*zWidth)
		var x = 0
		while(x < xWidth) {
			var z = 0
			while(z < zWidth) {
				val col = new Array[T](yWidth)
				var y = 0
				while(y < yWidth) {
					col(y) = compute(x+xFrom, y+yFrom, z+zFrom)
					y += 1
				}
				retval(x+z*xWidth) = col
				z += 1
			}
			x += 1
		}
		retval
	}
	def apply(x: Int, y: Int, z: Int): T = values((x-xFrom)+(z-zFrom)*xWidth)(y-yFrom)
}
