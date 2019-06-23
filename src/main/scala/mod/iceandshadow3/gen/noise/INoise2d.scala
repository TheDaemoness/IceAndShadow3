package mod.iceandshadow3.gen.noise

import mod.iceandshadow3.gen.FixedMap2d

trait INoise2d {
	def apply(x: Int, z: Int): Double = apply(x,z,1,1)(x,z)
	def apply(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int): FixedMap2d[Double]
}