package mod.iceandshadow3.gen

import java.util.Random

import mod.iceandshadow3.spatial.{RandomXYZ, TriadXYZ}
import Cellmaker._

class Cellmaker3d(
	protected val seed: Long,
	protected val mod: Int,
	protected val scaleXZ: Int,
	protected val scaleY: Int
) {
	protected val totalScale = Math.sqrt(scaleXZ*scaleXZ + scaleY*scaleY)
	def cellToPoint(xCell: Int, yCell: Int, zCell: Int, rng: Random): TriadXYZ = {
		val x = rng.nextInt(scaleXZ) + xCell*scaleXZ - (scaleXZ>>1)
		val y = rng.nextInt(scaleY) + yCell*scaleY - (scaleY>>1)
		val z = rng.nextInt(scaleXZ) + zCell*scaleXZ - (scaleXZ>>1)
		TriadXYZ(x,y,z)
	}

	def apply(x: Int, y: Int, z: Int): Result = {
		apply(x, x+1, y, y+1, z, z+1)(0)(0)(0)
	}
	def apply(
		xFrom: Int, yFrom: Int, zFrom: Int,
		xUntil: Int, yUntil: Int, zUntil: Int
	): Array[Array[Array[Result]]] = {
		val xCellLowest = Cellmaker.rescale(xFrom, scaleXZ)
		val yCellLowest = Cellmaker.rescale(yFrom, scaleY)
		val zCellLowest = Cellmaker.rescale(zFrom, scaleXZ)
		val xCellCount = Cellmaker.rescale(xUntil, scaleXZ)-xCellLowest+1
		val yCellCount = Cellmaker.rescale(yUntil, scaleY)-yCellLowest+1
		val zCellCount = Cellmaker.rescale(zUntil, scaleXZ)-zCellLowest+1
		val cellPoints = Array.tabulate[TriadXYZ](xCellCount+2, yCellCount+2, zCellCount+2)((xit, yit, zit) => {
			val xc = xit+xCellLowest-1
			val yc = yit+yCellLowest-1
			val zc = zit+zCellLowest-1
			val rng = new RandomXYZ(seed, mod, xc, yc, zc)
			cellToPoint(xc, yc, zc, rng)
		})
		Array.tabulate[Result](xUntil-xFrom, yUntil-yFrom, zUntil-zFrom)((xRela: Int, yRela: Int, zRela: Int) => {
			val x = xRela+xFrom
			val y = yRela+yFrom
			val z = zRela+zFrom
			val xCellIndex = Cellmaker.rescale(x, scaleXZ)-xCellLowest+1
			val yCellIndex = Cellmaker.rescale(y, scaleY)-yCellLowest+1
			val zCellIndex = Cellmaker.rescale(z, scaleXZ)-zCellLowest+1
			val result = new Result()
			for(xit <- xCellIndex-1 to xCellIndex+1) {
				for(yit <- yCellIndex-1 to yCellIndex+1) {
					for(zit <- zCellIndex-1 to zCellIndex+1) {
						val point = cellPoints(xit)(yit)(zit)
						val xDelta = point.x - x
						val yDelta = point.y - y
						val zDelta = point.z - z
						val value = (xDelta*xDelta + yDelta*yDelta + zDelta*zDelta)/totalScale
						result.update(value, xit+xCellLowest-1, yit+yCellLowest-1, zit+zCellLowest-1)
					}
				}
			}
			result
		})
	}
}
