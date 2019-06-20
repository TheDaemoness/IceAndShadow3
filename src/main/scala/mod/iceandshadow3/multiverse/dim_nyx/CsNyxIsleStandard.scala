package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.gen.Cellmaker.Result
import mod.iceandshadow3.gen.{Cellmaker, Cellmaker2d, Cellmaker3d, FixedMap2d}
import mod.iceandshadow3.util.MathUtils

class CsNyxIsleStandard(noises: NoisesNyx, cells: FixedMap2d[Result])
	extends BCsNyxIsleMountain(noises, cells)
{
	val yFissureMax = 172
	val yCaveMax = 148
	def yFissureFull = 148

	private def combine(a: Cellmaker3d, b: Cellmaker3d, limit: Int, fn: (Double, Double) => Float) = {
		val resultsA = a(xFrom, 0, zFrom, xWidth, limit, zWidth)
		val resultsB = b(xFrom, 0, zFrom, xWidth, limit, zWidth)
		new FixedMap2d[Array[Float]](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			Array.tabulate[Float](limit)(y => fn(
				Cellmaker.distance(resultsA(x, y, z)),
				Cellmaker.distance(resultsB(x, y, z))
			))
		})
	}
	protected lazy val fissuremap = combine(noises.fissuremakerMinor, noises.fissuremakerMajor, yFissureMax, (a, b) => {
		Math.min(a*a, b).toFloat
	})
	protected lazy val cavemap = combine(noises.cavemakerA, noises.cavemakerB, yCaveMax, (a,b) => {
		(a*b).toFloat
	})

	override def caveGen(height: Float, x: Int, z: Int): Seq[Boolean] = {
		val fissures = fissuremap(x,z)
		val caves = cavemap(x,z)
		val caveLimitReal = Math.min(yCaveMax, height-4).toInt
		Array.tabulate(256)(y => {
			val fissureAttenUpper = Math.sqrt(MathUtils.attenuateThrough(yFissureFull, y, yFissureMax))
			val fissureAttenLower = 1-MathUtils.attenuateThrough((height*0.6f).toInt, y, (height*0.8f).toInt)
			val caveAtten = MathUtils.attenuateThrough(Math.max(0, caveLimitReal - 10), y, caveLimitReal)
			//WARNING: Short-circuit evaluation.
				y < yFissureMax && fissures(y)*fissureAttenLower > (1 - fissureAttenUpper * 0.2) ||
					y < yCaveMax && caves(y) > (1 - caveAtten * 0.2)
			}
		)
	}
}
