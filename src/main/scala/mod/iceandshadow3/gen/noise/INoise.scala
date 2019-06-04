package mod.iceandshadow3.gen.noise

trait INoise {
	def height: Int
	def apply(x: Int, z: Int): Array[Double]
}