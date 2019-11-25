package mod.iceandshadow3.lib.spatial

trait IRegion3d extends IRegion2d {
	def yFrom: Int
	def yMax: Int
	def yWidth: Int
	def isInside(x: Int, y: Int, z: Int): Boolean =
		isInside(x, z) && y >= yFrom && y <= yMax
	def corners3d = (
		TupleXYZ(xFrom, yFrom, zFrom),
		TupleXYZ(xFrom, yFrom, zMax),
		TupleXYZ(xFrom, yMax,  zFrom),
		TupleXYZ(xMax,  yFrom, zFrom),
		TupleXYZ(xMax,  yMax,  zMax),
		TupleXYZ(xMax,  yMax,  zFrom),
		TupleXYZ(xMax,  yFrom, zMax),
		TupleXYZ(xFrom, yMax,  zMax)
	)
}
