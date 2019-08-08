package mod.iceandshadow3.lib.spatial

import java.util.Random

import mod.iceandshadow3.lib.util.IntBitUtils

object RandomXZ {
	def calculateSeed(seedIn: Long, modifierIn: Int, xIn: Int, zIn: Int) = {
		seedIn ^ new Random(~modifierIn ^ IntBitUtils.mixIntBits(xIn, zIn)).nextLong()
	}
}
import RandomXZ._

/** Implements a wrapper for a PRNG designed for providing random numbers at certain 2d coordinates.
	* Extends Java's Random class, which is an LCG.
	*/
class RandomXZ(seed: Long, modifier: Int, x: Int, z: Int)
	extends Random(calculateSeed(seed, modifier, x, z))
{
	def setSeed(x: Int, z: Int): Unit = {
		this.setSeed(calculateSeed(seed, modifier, x, z))
	}
}
