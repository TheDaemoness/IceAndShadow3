package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.util.collect.FixedMap2d

class Noise2dTfLayered(val maxheight: Int, sources: INoise2d*) extends INoise2d {
	override def apply(xFrom: Int, zFrom: Int, xWidth: Int = 1, zWidth: Int = 1): FixedMap2d[Double] = {
		val noises = sources.map(_.apply(xFrom, zFrom, xWidth, zWidth))
		new FixedMap2d[Double](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			var totaldivisor = 0d
			var result = 0d
			for(i <- noises.indices) {
				val divisor = 1d/(2 << i)
				totaldivisor += divisor
				result += noises(i)(x,z)
			}
			result/totaldivisor
		})
	}
}
