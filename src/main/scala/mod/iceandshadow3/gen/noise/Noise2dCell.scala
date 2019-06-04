package mod.iceandshadow3.gen.noise

import mod.iceandshadow3.gen.{Cellmaker, Cellmaker2d}

/** Variant of cell (worley) noise.
	*/
class Noise2dCell(seed: Long, mod: Int, scale: Int) extends INoise {
	val cellmaker = new Cellmaker2d(seed, mod, scale)
	override def height = 1
	override def apply(x: Int, z: Int): Array[Double] = {
		Array(Cellmaker.distance(cellmaker(x,z)))
	}
}
