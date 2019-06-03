package mod.iceandshadow3.spatial

import mod.iceandshadow3.util.SIntBits

/** An XZ pairing with no defined unit vectors.
	*/
object PairXZ {
	def rescale(in: Int, scale: Int): Int = {
		val inMod = in+scale/2
		inMod / scale - (if (inMod < 0) 1 else 0)
	}
}
case class PairXZ(private var _x: Int, private var _z: Int)
	extends Iterable[Int]
{
	def x = _x
	def z = _z
	def set(newX: Int, newZ: Int): Unit = {
		_x = newX
		_z = newZ
	}

	override def hashCode() = SIntBits.mixIntBits(_z,_x).toInt

	override def iterator: Iterator[Int] = new Iterator[Int]() {
		var index: Int = 0
		override def hasNext = index < 2

		override def next() = {
			index += 1
			if(index < 2) _x else _z
		}
	}
}
