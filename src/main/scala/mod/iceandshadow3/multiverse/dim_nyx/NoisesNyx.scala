package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.gen.noise.{Noise2dCell, NoiseTransformLayered}
import mod.iceandshadow3.gen.{Cellmaker2d, Cellmaker3d}
import mod.iceandshadow3.spatial.PairXZ

class NoisesNyx(val seed: Long) {
	val isleMaker = new Cellmaker2d(seed, 9967, 1200) {
		override def cellToPoint(xCell: Int, zCell: Int, rng: Random) = {
			if(xCell == 0 && zCell == 0) PairXZ(0,0)
			else super.cellToPoint(xCell, zCell, rng)
		}
	}
	val noisemakerDip = new NoiseTransformLayered(1,
		new Noise2dCell(seed, 4921, 45),
		new Noise2dCell(seed, 1936, 60)
	)
	val noisemakerMountain = new Noise2dCell(seed, 3092, 150)
	val noisemakerRidgeScale = new Noise2dCell(seed, 4815, 420)
	val noisemakerRidge = new Noise2dCell(seed, 6872, 230)
	val noisemakerHills = new Noise2dCell(seed, 7053, 125)

	val fissuremakerA = new Cellmaker3d(seed, 2391, 50, 14)
	val fissuremakerB = new Cellmaker3d(seed, 4672, 150, 19)

	val cavemakerA = new Cellmaker3d(seed, 2928, 30, 60)
	val cavemakerB = new Cellmaker3d(seed, 9478, 50, 75)
}
