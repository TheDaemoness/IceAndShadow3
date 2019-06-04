package mod.iceandshadow3.spatial

import java.util.Random

// Salvaged from IaS2 by its author.

object RandomXZ {
	def calculateSeed(seedIn: Long, modifierIn: Int, xIn: Int, zIn: Int) = {
		val x = java.lang.Double.doubleToLongBits(Math.expm1(xIn)).toInt
		val z = java.lang.Double.doubleToLongBits(Math.expm1(zIn)).toInt
		val modifier = new Random(modifierIn.toLong + (x * z).toLong + x + z).nextInt
		val tempseed = (seedIn >>> (modifier & 63)) | (seedIn << (64 - (modifier & 63)))
		tempseed ^ ((modifier ^ x) | ((~modifier ^ z).toLong << 32))
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
