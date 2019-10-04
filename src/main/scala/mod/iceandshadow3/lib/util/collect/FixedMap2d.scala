package mod.iceandshadow3.lib.util.collect

/** A read-only parallel-computed associative map, mapping 2d coordinates to values in O(1) time.
	*/
class FixedMap2d[T](
	val xFrom: Int, val zFrom: Int,
	val xWidth: Int, val zWidth: Int,
	compute: (Int,Int) => T
) extends IMap2d[T] {
	def this(xWidth: Int, zWidth: Int, compute: (Int,Int) => T) = this(0, 0, xWidth, zWidth, compute)
	val values = (0 until xWidth*zWidth).map(i => compute(xFrom+(i%xWidth), zFrom+(i/xWidth)))
	override def apply(x:Int, z:Int): T = values((x-xFrom)+(z-zFrom)*xWidth)
}
