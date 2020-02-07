package mod.iceandshadow3.lib.spatial

trait IMap2d[@specialized(Float) +T] {
	def apply(x: Int, z: Int): T
	def apply(pair: TupleXZ): T = apply(pair.x, pair.z)
}

trait IRange2d {
	def xWidth: Int
	def zWidth: Int
}

trait IRegion2d extends IRange2d {
	def xFrom: Int
	def zFrom: Int
	def xMax: Int
	def zMax: Int
	final def isInside(x: Int, z: Int) =
		x >= xFrom && z >= zFrom &&
			x <= xMax  && z <= zMax
	def corners2d = (
		TupleXZ(xFrom, zFrom),
		TupleXZ(xFrom, zMax),
		TupleXZ(xMax, zFrom),
		TupleXZ(xMax, zMax)
	)
}
