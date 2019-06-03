package mod.iceandshadow3.world.dim_nyx

import mod.iceandshadow3.gen.noise.{BNoise2dFractal, Noise2dCrater}
import mod.iceandshadow3.gen.{Cellmaker, XZRandom}
import mod.iceandshadow3.spatial.PairXZ

class NoisesNyx(seed: Long) {
	val isleMaker = new Cellmaker(seed, 9967, 1200) {
		override def cellToPoint(cell: PairXZ, rng: XZRandom) = {
			if(cell.x == 0 && cell.z == 0) PairXZ(0,0)
			else super.cellToPoint(cell, rng)
		}
	}
	val noisemakerDip = new BNoise2dFractal(seed, 1928, 2) {
		override protected def noisemaker(seed: Long, mod: Int, scale: Int) = new Noise2dCrater(seed, mod, scale*30)
	}
	val noisemakerMountain = new Noise2dCrater(seed, 3092, 150)
	val noisemakerRidgeScale = new Noise2dCrater(seed, 4815, 420)
	val noisemakerRidge = new Noise2dCrater(seed, 6872, 230)
}
