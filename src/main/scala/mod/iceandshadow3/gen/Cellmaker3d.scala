package mod.iceandshadow3.gen

import java.util.Random

import mod.iceandshadow3.spatial.{RandomXYZ, TriadXYZ}
import Cellmaker._
import mod.iceandshadow3.util.collect.FixedMap3d

import scala.reflect.ClassTag

class Cellmaker3d(
	protected val seed: Long,
	protected val mod: Int,
	protected val scaleXZ: Int,
	protected val scaleY: Int
) {
	protected val totalScale = Math.sqrt(scaleXZ*scaleXZ + scaleY*scaleY)
	private val totalScaleInv = 1f/totalScale

	def cellToPoint(xCell: Int, yCell: Int, zCell: Int, rng: Random): TriadXYZ = {
		val x = rng.nextInt(scaleXZ) + xCell*scaleXZ - (scaleXZ>>1)
		val y = rng.nextInt(scaleY) + yCell*scaleY - (scaleY>>1)
		val z = rng.nextInt(scaleXZ) + zCell*scaleXZ - (scaleXZ>>1)
		TriadXYZ(x,y,z)
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
		val xCellLowest = Cellmaker.rescale(xFrom, scaleXZ)
		val yCellLowest = Cellmaker.rescale(yFrom, scaleY)
		val zCellLowest = Cellmaker.rescale(zFrom, scaleXZ)
		val xCellCount = Cellmaker.rescale(xFrom+xWidth, scaleXZ)-xCellLowest+1
		val yCellCount = Cellmaker.rescale(yFrom+yWidth, scaleY)-yCellLowest+1
		val zCellCount = Cellmaker.rescale(zFrom+zWidth, scaleXZ)-zCellLowest+1
		val cells = new FixedMap3d[TriadXYZ](
			xCellLowest-1, yCellLowest-1, zCellLowest-1,
			xCellCount+2, yCellCount+2, zCellCount+2,
			(xit, yit, zit) => {
			val rng = new RandomXYZ(seed, mod, xit, yit, zit)
			cellToPoint(xit, yit, zit, rng)
		})
		new FixedMap3d[T](xFrom, yFrom, zFrom, xWidth, yWidth, zWidth, (x: Int, y: Int, z: Int) => {
			val xCellBase = Cellmaker.rescale(x, scaleXZ)
			val yCellBase = Cellmaker.rescale(y, scaleY)
			val zCellBase = Cellmaker.rescale(z, scaleXZ)
			val result = new Result()
			for(yit <- yCellBase-1 to yCellBase+1) {
				for(xit <- xCellBase-1 to xCellBase+1) {
					for(zit <- zCellBase-1 to zCellBase+1) {
						val point = cells(xit, yit, zit)
						val xDelta = point.x - x
						val yDelta = point.y - y
						val zDelta = point.z - z
						val value = xDelta*xDelta + yDelta*yDelta + zDelta*zDelta
						result.update(value, xit, yit, zit)
					}
				}
			}
			result.distanceClosest *= totalScaleInv
			result.distanceSecond *= totalScaleInv
			transform(result)
		})
	}
}
