package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.lib.spatial.PairXZ

trait IMap2d[T] {
	def apply(x: Int, z: Int): T
	def apply(pair: PairXZ): T = apply(pair.x, pair.z)
}
