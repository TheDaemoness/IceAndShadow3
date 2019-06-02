package mod.iceandshadow3.gen.noise

trait INoise2d {
	def apply(x: Int, z: Int): Double
}
trait INoise3d {
	def apply(x: Int, z: Int): Array[Double]
}