package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.util.collect.FixedMap2d

trait INoise2d {
	def apply(x: Int, z: Int): Float = apply(x,z,1,1)(x,z)
	def apply(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int): FixedMap2d[Float]
}
