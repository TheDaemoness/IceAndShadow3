package mod.iceandshadow3.lib.gen

import java.util.Random

import mod.iceandshadow3.lib.spatial.Cells._
import mod.iceandshadow3.lib.spatial.{Cells, RandomXYZ, TupleXYZ}
import mod.iceandshadow3.lib.util.collect.FixedMap3d

import scala.reflect.ClassTag

class Cellmaker3d(
	protected val seed: Long,
	protected val mod: Int,
	protected val scaleXZ: Int,
	protected val scaleY: Int
) {
	protected val totalScale = Math.sqrt(scaleXZ*scaleXZ + scaleY*scaleY).toFloat
	private val totalScaleInv = 1f/totalScale

	def cellToPoint(xCell: Int, yCell: Int, zCell: Int, rng: Random): TupleXYZ = {
		val x = rng.nextInt(scaleXZ) + xCell*scaleXZ - (scaleXZ>>1)
		val y = rng.nextInt(scaleY) + yCell*scaleY - (scaleY>>1)
		val z = rng.nextInt(scaleXZ) + zCell*scaleXZ - (scaleXZ>>1)
		TupleXYZ(x,y,z)
	}
	def apply(x: Int, y: Int, z: Int): Result = {
		apply(x, x+1, y, y+1, z, z+1)(x, y, z)
	}
	def apply(
		xFrom: Int, yFrom: Int, zFrom: Int,
		xWidth: Int, yWidth: Int, zWidth: Int,
	): FixedMap3d[Result] = apply(xFrom, yFrom, zFrom, xWidth, yWidth, zWidth, in => in)
	def apply[T: ClassTag](
		xFrom: Int, yFrom: Int, zFrom: Int,
		xWidth: Int, yWidth: Int, zWidth: Int,
		transform: Result => T
	): FixedMap3d[T] = {
		val xCellLowest = Cells.rescale(xFrom, scaleXZ)
		val yCellLowest = Cells.rescale(yFrom, scaleY)
		val zCellLowest = Cells.rescale(zFrom, scaleXZ)
		val xCellCount = Cells.rescale(xFrom+xWidth, scaleXZ)-xCellLowest+1
		val yCellCount = Cells.rescale(yFrom+yWidth, scaleY)-yCellLowest+1
		val zCellCount = Cells.rescale(zFrom+zWidth, scaleXZ)-zCellLowest+1
		val cells = new FixedMap3d[TupleXYZ](
			xCellLowest-1, yCellLowest-1, zCellLowest-1,
			xCellCount+2, yCellCount+2, zCellCount+2,
			(xit, yit, zit) => {
			val rng = new RandomXYZ(seed, mod, xit, yit, zit)
			cellToPoint(xit, yit, zit, rng)
		})
		new FixedMap3d[T](xFrom, yFrom, zFrom, xWidth, yWidth, zWidth, (x: Int, y: Int, z: Int) => {
			val xCellBase = Cells.rescale(x, scaleXZ)
			val yCellBase = Cells.rescale(y, scaleY)
			val zCellBase = Cells.rescale(z, scaleXZ)
			val result = new Result()
			var yit = yCellBase - 1
			while(yit <= yCellBase + 1) {
				var xit = xCellBase - 1
				while(xit <= xCellBase + 1) {
					var zit = zCellBase - 1
					while(zit <= zCellBase + 1) {
						val point = cells(xit, yit, zit)
						val xDelta = point.x - x
						val yDelta = point.y - y
						val zDelta = point.z - z
						val value = xDelta*xDelta + yDelta*yDelta + zDelta*zDelta
						result.update(value, xit, yit, zit)
						zit += 1
					}
					xit += 1
				}
				yit += 1
			}
			result.distanceClosest *= totalScaleInv
			result.distanceSecond *= totalScaleInv
			transform(result)
		})
	}
}
