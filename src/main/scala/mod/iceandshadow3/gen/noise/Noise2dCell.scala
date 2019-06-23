package mod.iceandshadow3.gen.noise

import mod.iceandshadow3.gen.{Cellmaker, Cellmaker2d, FixedMap2d}

/** Variant of cell (worley) noise.
	*/
class Noise2dCell(seed: Long, mod: Int, scale: Int) extends INoise2d {
	val cellmaker = new Cellmaker2d(seed, mod, scale)
	override def apply(xFrom: Int, zFrom: Int, xWidth: Int = 1, zWidth: Int = 1): FixedMap2d[Double] =
		cellmaker(xFrom,zFrom,xWidth,zWidth,Cellmaker.distance)
}
