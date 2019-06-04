package mod.iceandshadow3.spatial

import java.util.Random

import mod.iceandshadow3.util.SIntBits

object RandomXYZ {
	def calculateSeed(seedIn: Long, modifierIn: Int, xIn: Int, yIn: Int, zIn: Int) = {
		val xz = new Random(SIntBits.mixIntBits(zIn, xIn)).nextLong()
		val y = new Random(SIntBits.mixIntBits(modifierIn, yIn)).nextLong()
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
