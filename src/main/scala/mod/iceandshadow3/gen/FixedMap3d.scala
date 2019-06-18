package mod.iceandshadow3.gen

import mod.iceandshadow3.util.DenseIntArrayEncoder

import scala.collection.parallel.mutable.ParArray
import scala.reflect.ClassTag

/** A read-only parallel-computed associative map, mapping 3d coordinates to values in O(1) time.
	*/
class FixedMap3d[T: ClassTag](val xWidth: Int, val yWidth: Int, val zWidth: Int, tsGenerator: (Int, Int, Int) => T) {
	val arrays = ParArray.tabulate[T](xWidth*zWidth, yWidth)((xzi, yit) => {
		val xit = xzi%xWidth
		val zit = xzi/xWidth
		tsGenerator(xit, yit, zit)
	})
	def apply(x: Int, y: Int, z: Int): T = arrays(x+z*xWidth)(y)
}
