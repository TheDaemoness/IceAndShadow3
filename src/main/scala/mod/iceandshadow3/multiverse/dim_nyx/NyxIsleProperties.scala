package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.spatial.{ITupleXZ, RandomXZ}

case class NyxIsleProperties private(
	private val treesA: Boolean,
	private val treesB: Boolean
	//TODO: An enumeration of the possible tree types.
) {
	def treesSparse = treesA || treesB
	def treesDense = treesA && treesB
}
object NyxIsleProperties {
	def apply(seed: Long, isle: ITupleXZ) = {
		val rng = new RandomXZ(seed, 99414, isle.x, isle.z)
		new NyxIsleProperties(
			treesA = (isle.x == 0 && isle.z == 0) || rng.nextBoolean(),
			treesB = (isle.x == 0 && isle.z == 0) || rng.nextBoolean()
		)
	}
}
