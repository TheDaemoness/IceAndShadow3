package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.gen.{Cellmaker2d, Cellmaker3d, Noise2dCell, Noise2dTfLayered}
import mod.iceandshadow3.lib.spatial.TupleXZ

/** A collection of all the number generators Nyx worldgen is likely to need.
	*/
class NoisesNyx(val seed: Long) {
	val isleMaker = new Cellmaker2d(seed, 9967, 1200) {
		override def cellToPoint(xCell: Int, zCell: Int, rng: Random) = {
			if (xCell == 0 && zCell == 0) TupleXZ(0, 0)
			else super.cellToPoint(xCell, zCell, rng)
		}
	}

	val noisemakerDip = new Noise2dTfLayered(1,
		new Noise2dCell(seed, 4921, 45),
		new Noise2dCell(seed, 1936, 60)
	)
	val noisemakerMountain = new Noise2dCell(seed, 3092, 150)
	val noisemakerRidgeScale = new Noise2dCell(seed, 4815, 420)
	val noisemakerRidge = new Noise2dCell(seed, 6872, 230)
	val noisemakerHills = new Noise2dCell(seed, 7053, 150)

	val fissuremakerMinor = new Cellmaker3d(seed, 2391, 100, 50)
	val fissuremakerMajor = new Cellmaker3d(seed, 4672, 250, 100)

	val cavemakerA = new Cellmaker3d(seed, 2928, 30, 60)
	val cavemakerB = new Cellmaker3d(seed, 9478, 50, 75)

	val stonemakerUpper = new Cellmaker2d(seed, 72346, 300)
	val stonemakerLower = new Cellmaker2d(seed, 19203, 150)
}
