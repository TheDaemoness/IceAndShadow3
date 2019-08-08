package mod.iceandshadow3.gen

// ~~Makes cells for someone to dwell in.~~

import java.util.Random

import mod.iceandshadow3.lib.spatial.Cells._
import mod.iceandshadow3.lib.spatial.{Cells, PairXZ, RandomXZ}
import mod.iceandshadow3.lib.util.collect.FixedMap2d

import scala.reflect.ClassTag

class Cellmaker2d(
	protected val seed: Long,
	protected val mod: Int,
	protected val scale: Int
) {
	protected val scaleInv = 1f/scale
	def cellToPoint(xCell: Int, zCell: Int, rng: Random): PairXZ = {
		val x = rng.nextInt(scale) + Cells.cellEdge(scale, xCell)
		val z = rng.nextInt(scale) + Cells.cellEdge(scale, zCell)
		PairXZ(x,z)
	}
	def getInverseWeightForCell(xCell:Int, zCell:Int): Double = 1d
	def apply(x: Int, z: Int): Result = apply(x, z, 1, 1)(x, z)
	def apply(
		xFrom: Int, zFrom: Int,
		xWidth: Int, zWidth: Int
	): FixedMap2d[Result] = apply(xFrom, zFrom, xWidth, zWidth, in => in)
	def apply[T: ClassTag](
		xFrom: Int, zFrom: Int,
		xWidth: Int, zWidth: Int,
		transform: Result => T
	): FixedMap2d[T] = {
		new FixedMap2d[T](xFrom, zFrom, xWidth, zWidth, (x: Int, z: Int) => {
			val xCellBase = Cells.rescale(x, scale)
			val zCellBase = Cells.rescale(z, scale)
			val result = new Result()
			for(xit <- xCellBase-1 to xCellBase+1) {
				for(zit <- zCellBase-1 to zCellBase+1) {
					val point = cellToPoint(xit, zit, new RandomXZ(seed, mod, xit, zit))
					val xDelta = point.x - x
					val zDelta = point.z - z
					val distance = (xDelta*xDelta + zDelta*zDelta) * getInverseWeightForCell(xit, zit)
					result.update(distance, xit, 0, zit)
				}
			}
			result.distanceClosest *= scaleInv
			result.distanceSecond *= scaleInv
			transform(result)
		})
	}
}
