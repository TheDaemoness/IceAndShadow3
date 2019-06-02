package mod.iceandshadow3.gen.noise

abstract class BNoise2dFractal(seed: Long, mod: Int, octaves: Int) extends INoise2d {
	protected def noisemaker(seed: Long, mod: Int, scale: Int): INoise2d
	val sources = Array.tabulate[INoise2d](octaves){i => noisemaker(seed, mod^i, 1 << (octaves-i-1))}
	override def apply(x: Int, z: Int): Double = {
		var total = 0d
		var totaldivisor = 0d
		for(i <- sources.indices) {
			val divisor = 1d/(2 << i)
			total += sources(i)(x,z)*divisor
			totaldivisor += divisor
		}
		total / totaldivisor
	}
}
