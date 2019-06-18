package mod.iceandshadow3.spatial

case class TriadXYZ(private var _x: Int, private var _y: Int, private var _z: Int)
	extends Iterable[Int]
{
	def this(xz: PairXZ, y: Int = 0) = this(xz.x, y, xz.z)
	def x = _x
	def y = _y
	def z = _z
	def set(newX: Int, newY: Int, newZ: Int): Unit = {
		_x = newX
		_y = newY
		_z = newZ
	}

	override def toString() = s"[$x,$y,$z]"

	override def iterator: Iterator[Int] = new Iterator[Int]() {
		var index: Int = 0
		override def hasNext = index < 3

		override def next() = {
			index += 1
			index match {
				case 1 => _x
				case 2 => _y
				case 3 => _z
			}
		}
	}
}
