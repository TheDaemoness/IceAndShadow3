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
	private val totalScaleInv = 1f/totalScale

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
		val cells = new Collection3d[TriadXYZ](xCellCount+2, yCellCount+2, zCellCount+2, (xit, yit, zit) => {
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
			val xCellBase = Cellmaker.rescale(x, scaleXZ)-1
			val yCellBase = Cellmaker.rescale(y, scaleY)-1
			val zCellBase = Cellmaker.rescale(z, scaleXZ)-1
			val result = new Result()
			for(yi <- 0 to 2) {
				for(xi <- 0 to 2) {
					for(zi <- 0 to 2) {
						val point = cells(xi, yi, zi)
						val xDelta = point.x - x
						val yDelta = point.y - y
						val zDelta = point.z - z
						val value = (xDelta*xDelta + yDelta*yDelta + zDelta*zDelta) * totalScaleInv
						result.update(value, xi+xCellBase, yi+yCellBase, zi+zCellBase)
					}
				}
			}
			result
		})
	}
}
