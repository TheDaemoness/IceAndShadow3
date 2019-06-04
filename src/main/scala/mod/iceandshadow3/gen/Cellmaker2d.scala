package mod.iceandshadow3.gen

// ~~Makes cells for someone to dwell in.~~

import java.util.Random

import mod.iceandshadow3.spatial.{PairXZ, RandomXZ}
import mod.iceandshadow3.util.concurrent.Materializer
import Cellmaker._

class Cellmaker2d(
	protected val seed: Long,
	protected val mod: Int,
	protected val scale: Int
) {
	def cellToPoint(cell: PairXZ, rng: Random): PairXZ = {
		val x = rng.nextInt(scale) + cell.x*scale - (scale>>1)
		val z = rng.nextInt(scale) + cell.z*scale - (scale>>1)
		PairXZ(x,z)
	}
	val pointsource = new Materializer[PairXZ, PairXZ]((cell: PairXZ) => {
		val rng = new RandomXZ(seed, mod, cell.x, cell.z)
		cellToPoint(cell, rng)
	}, 25)
	def getWeightForCell(xCell:Int, zCell:Int): Double = 1d
	def apply(x: Int, z: Int): Result = {
		val xCell = Cellmaker.rescale(x, scale)
		val zCell = Cellmaker.rescale(z, scale)
		val cellResult = new Result
		for (xit <- xCell - 1 to xCell + 1) {
			for (zit <- zCell - 1 to zCell + 1) {
				val cellpoint = pointsource(PairXZ(xit,zit))
				val xDelta = cellpoint.x - x
				val zDelta = cellpoint.z - z
				val value = (xDelta * xDelta + zDelta * zDelta) / (scale*getWeightForCell(xit, zit))
				cellResult.update(value, xit, 0, zit)
			}
		}
		cellResult
	}
}
