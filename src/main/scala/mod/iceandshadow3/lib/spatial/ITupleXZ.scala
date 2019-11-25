package mod.iceandshadow3.lib.spatial

trait ITupleXZ extends Iterable[Int] {
	def x: Int
	def z: Int
	def x_=(what: Int): Unit
	def z_=(what: Int): Unit
	def update(b: TupleXZ): Unit = {
		x = b.x
		z = b.z
	}
}
