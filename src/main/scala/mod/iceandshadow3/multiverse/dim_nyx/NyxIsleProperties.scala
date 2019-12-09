package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.spatial.{ITupleXZ, RandomXZ}

case class NyxIsleProperties private(
	trees: Boolean //TODO: An enumeration for the possible tree types.
)
object NyxIsleProperties {
	def apply(seed: Long, isle: ITupleXZ) = {
		val rng = new RandomXZ(seed, 99414, isle.x, isle.z)
		new NyxIsleProperties(
			trees = (isle.x == 0 && isle.z == 0) || rng.nextBoolean()
		)
	}
}
