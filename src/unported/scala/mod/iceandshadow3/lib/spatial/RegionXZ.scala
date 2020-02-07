package mod.iceandshadow3.lib.spatial

case class RegionXZ(xFrom: Int, zFrom: Int, xMax: Int, zMax: Int)
extends IRegion2d with Iterable[TupleXZ] {
	override def xWidth = xMax-xFrom+1
	override def zWidth = zMax-zFrom+1
	def lower = TupleXZ(xFrom, zFrom)
	def upper = TupleXZ(xMax, zMax)

	override def iterator: Iterator[TupleXZ] = new Iterator[TupleXZ] {
		private var x = xFrom
		private var z = zFrom

		override def hasNext = x != xMax || z != zMax
		override def next() = {
			val tuple = TupleXZ(x, z)
			if(x == xMax) {
				z += 1
				x = xFrom
			} else x += 1
			tuple
		}
	}

	override def foreach[U](f: TupleXZ => U): Unit = {
		var mutable = TupleXZ(xFrom, zFrom)
		var x = xFrom
		while(x <= xMax) {
			var z = zFrom
			while(z <= zMax) {
				mutable.set(x, z)
				f(mutable)
				z += 1
			}
			x += 1
		}
	}
}

