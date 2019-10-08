package mod.iceandshadow3.lib.util.collect

import scala.reflect.ClassTag

/** A read-only associative map, mapping 2d coordinates to values in O(1) time.
	*/
class FixedMap2d[@specialized(Float) T: ClassTag](
	val xFrom: Int, val zFrom: Int,
	val xWidth: Int, val zWidth: Int,
	compute: (Int,Int) => T
) extends IMap2d[T] with IRegion2d {
	override def xMax = xFrom+xWidth-1
	override def zMax = zFrom+zWidth-1
	def this(xWidth: Int, zWidth: Int, compute: (Int,Int) => T) = this(0, 0, xWidth, zWidth, compute)
	val values: Array[T] = {
		val retval = new Array[T](xWidth*zWidth)
		var x = 0
		var z = 0
		while(x < xWidth) {
			while(z < zWidth) {
				retval(x+z*xWidth) = compute(x+xFrom, z+zFrom)
				z += 1
			}
			z = 0
			x += 1
		}
		retval
	}
	override def apply(x:Int, z:Int): T = values((x-xFrom)+(z-zFrom)*xWidth)
}
