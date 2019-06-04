package mod.iceandshadow3.world.dim_nyx

import java.util.Random

import mod.iceandshadow3.gen.noise.{Noise2dCell, NoiseTransformLayered}
import mod.iceandshadow3.gen.{Cellmaker2d, Cellmaker3d}
import mod.iceandshadow3.spatial.PairXZ

class NoisesNyx(val seed: Long) {
	val isleMaker = new Cellmaker2d(seed, 9967, 1200) {
		override def cellToPoint(cell: PairXZ, rng: Random) = {
			if(cell.x == 0 && cell.z == 0) PairXZ(0,0)
			else super.cellToPoint(cell, rng)
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

	val cavemakerA = new Cellmaker3d(seed, 2391, 100, 14)
	val cavemakerB = new Cellmaker3d(seed, 4672, 150, 19)
}
