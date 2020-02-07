package mod.iceandshadow3.lib.spatial

import mod.iceandshadow3.lib.util.IntBitUtils

/** An XZ pairing with no defined unit vectors.
	*/
case class TupleXZ(var x: Int, var z: Int)
	extends ITupleXZ {

	def set(x: Int, z: Int): Unit = {
		this.x = x
		this.z = z
	}
	def toTriad(y: Int): TupleXYZ = TupleXYZ(x, y, z)
	override def toString() = s"[$x,$z]"
	override def hashCode() = IntBitUtils.mixIntBits(z,x).toInt

	def copy: TupleXZ = new TupleXZ(x, z)
	override def iterator: Iterator[Int] = new Iterator[Int]() {
		var index: Int = 0
		override def hasNext = index < 2

		override def next() = {
			index += 1
			if(index < 2) x else z
		}
	}
}
