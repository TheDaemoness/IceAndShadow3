package mod.iceandshadow3.gen

// ~~Makes cells for someone to dwell in.~~

import mod.iceandshadow3.spatial.PairXZ
import mod.iceandshadow3.util.concurrent.Materializer

object Cellmaker {
	class Result {
		var distanceClosest = Double.PositiveInfinity
		var distanceSecond = Double.PositiveInfinity
		var cellClosest = PairXZ(0, 0)
		var cellSecond = PairXZ(0, 0)
		def makeRandom(seed: Long, mod: Int) = new XZRandom(seed, mod, cellClosest.x, cellClosest.z)
	}
}
import Cellmaker._
class Cellmaker(protected val seed: Long, protected val mod: Int, protected val scale: Int) {
	val halfscale = scale >> 1
	def cellToPoint(cell: PairXZ, rng: XZRandom): PairXZ = {
		val x = rng.nextInt(scale) + cell.x*scale - halfscale
		val z = rng.nextInt(scale) + cell.z*scale - halfscale
		PairXZ(x,z)
	}
	val pointsource = new Materializer[PairXZ, PairXZ]((cell: PairXZ) => {
		val rng = new XZRandom(seed, mod, cell.x, cell.z)
		cellToPoint(cell, rng)
	}, 25)
	def getWeightForCell(xCell:Int, zCell:Int): Double = 1d
	def apply(x: Int, z: Int): Result = {
		val xCell = PairXZ.rescale(x, scale)
		val zCell = PairXZ.rescale(z, scale)
		val cellResult = new Result
		for (xit <- xCell - 1 to xCell + 1) {
			for (zit <- zCell - 1 to zCell + 1) {
				val cellpoint = pointsource(PairXZ(xit,zit))
				val xDelta = cellpoint.x - x
				val zDelta = cellpoint.z - z
				val value = (xDelta * xDelta + zDelta * zDelta) / (scale*getWeightForCell(x, z))
				if (value < cellResult.distanceSecond) {
					if (value < cellResult.distanceClosest) {
						cellResult.distanceSecond = cellResult.distanceClosest
						cellResult.distanceClosest = value
						cellResult.cellSecond = cellResult.cellClosest
						cellResult.cellClosest.set(xit, zit)
					} else {
						cellResult.distanceSecond = value
						cellResult.cellSecond.set(xit, zit)
					}
				}
			}
		}
		cellResult
	}
}
