package mod.iceandshadow3.lib.spatial

case class RegionXYZ(xFrom: Int, yFrom: Int, zFrom: Int, xMax: Int, yMax: Int, zMax: Int)
extends IRegion3d with Iterable[TupleXYZ] {
	override def yWidth = yMax-yFrom+1
	override def xWidth = xMax-xFrom+1
	override def zWidth = zMax-zFrom+1
	def lower = TupleXYZ(xFrom, yFrom, zFrom)
	def upper = TupleXYZ(xMax, yMax, zMax)

	override def iterator: Iterator[TupleXYZ] = new Iterator[TupleXYZ] {
		private var x = xFrom
		private var y = yFrom
		private var z = zFrom

		override def hasNext = x != xMax || y != yMax || z != zMax
		override def next() = {
			val tuple = TupleXYZ(x, y, z)
			if(x == xMax) {
				if (z == zMax) {
					y += 1
					z = zFrom
				} else z += 1
				x = xFrom
			} else x += 1
			tuple
		}
	}

	override def foreach[U](f: TupleXYZ => U): Unit = {
		var mutable = new TupleXYZ(xFrom, yFrom, zFrom)
		var y = yFrom
		while(y <= yMax) {
			var x = xFrom
			while(x <= xMax) {
				var z = zFrom
				while(z <= zMax) {
					mutable.set(x, y, z)
					f(mutable)
					z += 1
				}
				x += 1
			}
			y += 1
		}
	}
}
object RegionXYZ {
	def apply(a: TupleXYZ, b: TupleXYZ) = new RegionXYZ(
		Math.min(a.x, b.x),
		Math.min(a.y, b.y),
		Math.min(a.z, b.z),
		Math.max(a.x, b.x),
		Math.max(a.y, b.y),
		Math.max(a.z, b.z)
	)
}
