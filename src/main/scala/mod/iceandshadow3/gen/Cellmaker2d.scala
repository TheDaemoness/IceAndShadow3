package mod.iceandshadow3.gen

// ~~Makes cells for someone to dwell in.~~

import java.util.Random
import java.util.concurrent.TimeUnit

import mod.iceandshadow3.spatial.{PairXZ, RandomXZ}
import Cellmaker._
import com.google.common.cache.{CacheBuilder, CacheLoader}

class Cellmaker2d(
	protected val seed: Long,
	protected val mod: Int,
	protected val scale: Int
) {
	protected val scaleInv = 1f/scale
	def cellToPoint(xCell: Int, zCell: Int, rng: Random): PairXZ = {
		val x = rng.nextInt(scale) + xCell*scale - (scale>>1)
		val z = rng.nextInt(scale) + zCell*scale - (scale>>1)
		PairXZ(x,z)
	}
	val cache = CacheBuilder.newBuilder().
		expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(25).build(
		new CacheLoader[PairXZ, PairXZ] {
			override def load(key: PairXZ) = {
				val rng = new RandomXZ(seed, mod, key.x, key.z)
				cellToPoint(key.x, key.z, rng)
			}
		}
	)
	def getInverseWeightForCell(xCell:Int, zCell:Int): Double = 1d
	def apply(x: Int, z: Int): Result = apply(x, z, x+1, z+1)(0)(0)
	def apply(
		xFrom: Int, zFrom: Int,
		xUntil: Int, zUntil: Int
	): Array[Array[Result]] = {
		Array.tabulate[Result](xUntil-xFrom, zUntil-zFrom)((xRela: Int, zRela: Int) => {
			val x = xRela+xFrom
			val z = zRela+zFrom
			val xCellBase = Cellmaker.rescale(x, scale)
			val zCellBase = Cellmaker.rescale(z, scale)
			val result = new Result()
			for(xit <- xCellBase-1 to xCellBase+1) {
				for(zit <- zCellBase-1 to zCellBase+1) {
					val point = cache.get(PairXZ(xit, zit))
					val xDelta = point.x - x
					val zDelta = point.z - z
					val value = (xDelta*xDelta + zDelta*zDelta) * getInverseWeightForCell(xit, zit) * scaleInv
					result.update(value, xit, 0, zit)
				}
			}
			result
		})
	}
}
