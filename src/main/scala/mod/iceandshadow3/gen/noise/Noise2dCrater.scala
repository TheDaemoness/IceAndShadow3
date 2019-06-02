package mod.iceandshadow3.gen.noise

import mod.iceandshadow3.gen.Cellmaker

/** Variant of cell (worley) noise.
	*/
object Noise2dCrater {
	def apply(result: Cellmaker.Result) =
		(2*result.distanceClosest)/(result.distanceClosest+result.distanceSecond)
}
class Noise2dCrater(seed: Long, mod: Int, scale: Int) extends INoise2d {
	val cellmaker = new Cellmaker(seed, mod, scale)
	override def apply(x: Int, z: Int): Double = {
		Noise2dCrater(cellmaker(x,z))
	}
}
