package mod.iceandshadow3.lib.spatial

import mod.iceandshadow3.lib.util.IntBitUtils

case class TupleXYZ(var x: Int, var y: Int, var z: Int)
	extends ITupleXZ
{
	def toPair: TupleXZ = TupleXZ(x, z)
	override def hashCode() = IntBitUtils.mixIntBits(z,x).toInt^y
	override def toString() = s"[$x,$y,$z]"

	def copy: TupleXYZ = new TupleXYZ(x, y, z)
	def set(newX: Int, newY: Int, newZ: Int): Unit = {
		x = newX
		y = newY
		z = newZ
	}
	def update(b: TupleXYZ): Unit = set(b.x, b.y, b.z)

	override def iterator: Iterator[Int] = new Iterator[Int]() {
		var index: Int = 0
		override def hasNext = index < 3

		override def next() = {
			index += 1
			index match {
				case 1 => x
				case 2 => y
				case 3 => z
			}
		}
	}
}
object TupleXYZ {
	def corners(a: ITupleXZ, b: ITupleXZ, yA: Int, yB: Int) = Array(
		TupleXYZ(a.x, yA, a.z),
		TupleXYZ(a.x, yA, b.z),
		TupleXYZ(a.x, yB, a.z),
		TupleXYZ(b.x, yA, a.z),
		TupleXYZ(b.x, yB, b.z),
		TupleXYZ(b.x, yB, a.z),
		TupleXYZ(b.x, yA, b.z),
		TupleXYZ(a.x, yB, b.z)
	)
	def corners(a: TupleXYZ, b: TupleXYZ): Array[TupleXYZ] = corners(a, b, a.y, b.y)
}
