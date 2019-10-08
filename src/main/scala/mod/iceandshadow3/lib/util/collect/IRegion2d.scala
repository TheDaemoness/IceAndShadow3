package mod.iceandshadow3.lib.util.collect

trait IRegion2d extends IRange2d {
	def xFrom: Int
	def zFrom: Int
	def xMax: Int
	def zMax: Int
	final def isInside(x: Int, z: Int) =
		x >= xFrom && z >= zFrom &&
			x <= xMax  && z <= zMax
}
