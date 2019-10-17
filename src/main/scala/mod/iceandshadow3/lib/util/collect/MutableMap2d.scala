package mod.iceandshadow3.lib.util.collect

import scala.collection.mutable

class MutableMap2d[T](
	val xFrom: Int, val zFrom: Int,
	val xWidth: Int, val zWidth: Int,
	defaultValue: T
) extends IMap2d[T] with IRegion2d {
	override def xMax = xFrom+xWidth-1
	override def zMax = zFrom+zWidth-1
	private val values = mutable.ArraySeq.untagged.fill(xWidth*zWidth)(defaultValue)
	override def apply(x:Int, z:Int): T = values((x-xFrom)+(z-zFrom)*xWidth)
	def update(x: Int, z: Int, value: T): Unit = values.update((x-xFrom)+(z-zFrom)*xWidth, value)
}
