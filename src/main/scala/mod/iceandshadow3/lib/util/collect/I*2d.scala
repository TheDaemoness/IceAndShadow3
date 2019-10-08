package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.lib.spatial.PairXZ

trait IMap2d[@specialized(Float) +T] {
	def apply(x: Int, z: Int): T
	def apply(pair: PairXZ): T = apply(pair.x, pair.z)
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
}
