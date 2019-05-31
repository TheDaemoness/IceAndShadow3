package mod.iceandshadow3.gen

/** Variant of cell (worley) noise.
	*/
class Noise2dCell(seed: Long, mod: Int, scale: Int) {
	def apply(x: Int, z: Int): Double = {
		val xCell = x/scale - (if(x<0) 1 else 0)
		val zCell = z/scale - (if(z<0) 1 else 0)
		var closest = Long.MaxValue
		var runnerup = Long.MaxValue
		for(xit <- xCell-1 to xCell+1) {
			for(zit <- zCell-1 to zCell+1) {
				val rng = new ColumnRandom(seed, mod, xit, zit)
				val xDelta = rng.nextInt(scale) + xit*scale - x
				val zDelta = rng.nextInt(scale) + zit*scale - z
				val value = xDelta*xDelta + zDelta*zDelta + 1
				if(value < runnerup) {
					if (value < closest) {
						runnerup = closest
						closest = value
					} else runnerup = value
				}
			}
		}
		1-2*closest.toDouble/(closest+runnerup)
	}
}
