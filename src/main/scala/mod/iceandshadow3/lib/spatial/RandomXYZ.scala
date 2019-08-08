package mod.iceandshadow3.lib.spatial

import java.util.Random

import mod.iceandshadow3.lib.util.IntBitUtils

object RandomXYZ {
	def calculateSeed(seedIn: Long, modifierIn: Int, xIn: Int, yIn: Int, zIn: Int) = {
		val xz = new Random(IntBitUtils.mixIntBits(zIn, xIn)).nextLong()
		val y = new Random(IntBitUtils.mixIntBits(modifierIn, yIn)).nextLong()
		seedIn ^ xz ^ y
	}
}
import RandomXYZ._

class RandomXYZ(seed: Long, modifier: Int, x: Int, y: Int, z: Int)
	extends Random(calculateSeed(seed, modifier, x, y, z))
	{
		def setSeed(x: Int, z: Int): Unit = {
			this.setSeed(calculateSeed(seed, modifier, x, y, z))
		}
	}
