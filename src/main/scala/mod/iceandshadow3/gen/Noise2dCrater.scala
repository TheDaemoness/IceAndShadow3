package mod.iceandshadow3.gen

/** Variant of cell (worley) noise.
	*/
class Noise2dCrater(seed: Long, mod: Int, scale: Int) {
	val cellmaker = new Cellmaker(seed, mod, scale)
	def apply(x: Int, z: Int): Double = {
		val result = cellmaker(x,z)
		(2*result.distanceClosest)/(result.distanceClosest+result.distanceSecond)
	}
}
