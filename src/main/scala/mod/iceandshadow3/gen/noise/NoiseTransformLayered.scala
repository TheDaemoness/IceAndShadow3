package mod.iceandshadow3.gen.noise

class NoiseTransformLayered(val maxheight: Int, sources: INoise*) extends INoise {
	val lowestheight = sources.foldLeft(maxheight)((height: Int, source: INoise) => {Math.min(height, source.height)})
	override def height = lowestheight
	override def apply(x: Int, z: Int): Array[Double] = {
		var total = Array.fill[Double](lowestheight)(0d)
		var totaldivisor = 0d
		for(i <- sources.indices) {
			val divisor = 1d/(2 << i)
			totaldivisor += divisor
			val noisiness = sources(i)(x,z)
			for(yit <- total.indices) total(yit) += noisiness(yit)*divisor
		}
		total.transform(_/totaldivisor)
		total
	}
}
