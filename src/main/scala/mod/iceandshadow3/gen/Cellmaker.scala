package mod.iceandshadow3.gen

// ~~Makes cells for someone to dwell in.~~

import mod.iceandshadow3.spatial.XZPair
import mod.iceandshadow3.util.concurrent.Materializer

object Cellmaker {
	class Result {
		var distanceClosest = Double.PositiveInfinity
		var distanceSecond = Double.PositiveInfinity
		var cellClosest = XZPair(0, 0)
		var cellSecond = XZPair(0, 0)
		def makeRandom(seed: Long, mod: Int) = new XZRandom(seed, mod, cellClosest.x, cellClosest.z)
	}
}
import Cellmaker._
class Cellmaker(protected val seed: Long, protected val mod: Int, protected val scale: Int) {
	val halfscale = scale >> 1
	def cellToPoint(cell: XZPair, rng: XZRandom): XZPair = {
		val x = rng.nextInt(scale) + cell.x*scale - halfscale
		val z = rng.nextInt(scale) + cell.z*scale - halfscale
		XZPair(x,z)
	}
	val pointsource = new Materializer[XZPair, XZPair]((cell: XZPair) => {
		val rng = new XZRandom(seed, mod, cell.x, cell.z)
		cellToPoint(cell, rng)
	}, 25)
	def getWeightForCell(xCell:Int, zCell:Int): Double = 1d
	def apply(x: Int, z: Int): Result = {
		val xCell = XZPair.rescale(x, scale)
		val zCell = XZPair.rescale(z, scale)
		val cellResult = new Result
		for (xit <- xCell - 1 to xCell + 1) {
			for (zit <- zCell - 1 to zCell + 1) {
				val cellpoint = pointsource(XZPair(xit,zit))
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
