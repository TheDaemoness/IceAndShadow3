package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.gen.{Cellmaker, Cellmaker3d, FixedMap2d}
import mod.iceandshadow3.util.MathUtils

class ChunksNyx(noises: NoisesNyx, xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int)
	extends BChunksNyx(noises, xFrom, zFrom, xWidth, zWidth)
{

	val yFissureMax = 172
	val yCaveMax = 148
	def yFissureFull = 148

	private def make3dMap(a: Cellmaker3d, b: Cellmaker3d, limit: Int) = {
		val resultsA = a(xFrom, 0, zFrom, xFrom+xWidth, limit, zFrom+zWidth)
		val resultsB = b(xFrom, 0, zFrom, xFrom+xWidth, limit, zFrom+zWidth)
		new FixedMap2d[Array[Float]](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			Array.tabulate[Float](limit)(y => (
				Cellmaker.distance(resultsA(x-xFrom)(y)(z-zFrom)) *
					Cellmaker.distance(resultsB(x-xFrom)(y)(z-zFrom))
				).toFloat
			)
		})
	}

	protected lazy val fissuremap = make3dMap(noises.fissuremakerA, noises.fissuremakerB, yFissureMax)
	protected lazy val cavemap = make3dMap(noises.cavemakerA, noises.cavemakerB, yCaveMax)

	override def genHeight(cell: Cellmaker.Result, x: Int, z: Int) = {
		import noises._
		val islevalue = 1-Cellmaker.distance(cell)
		val ridgescale = Math.sqrt(1-noisemakerRidgeScale(x,z)(0))
		var cratervalue = noisemakerDip(x,z)(0)
		cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
		val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerMountain(x,z)(0)*Math.PI)))/2
		val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerRidge(x,z)(0)*Math.PI)))/2
		var hillvalue = MathUtils.sinelike(1-noisemakerHills(x,z)(0))
		hillvalue *= hillvalue
		val retval =
			if(islevalue <= 0.15) 0
			else {
				val tuner = if(islevalue <= 0.3) (islevalue-0.2)*10 else 1d
				val totalmountainvalue = (ridgevalue+mountainvalue)*tuner*(1+islevalue)+hillvalue
				val base = 1.5+MathUtils.sinelike(islevalue)+totalmountainvalue+cratervalue
				if(islevalue <= 0.2) base*MathUtils.sinelike((islevalue-0.15)*20)
				else base
			}
		retval.toFloat
	}

	override def caveGen(cell: Cellmaker.Result, height: Float, x: Int, z: Int): Seq[Boolean] = {
		lazy val fissures = fissuremap(x,z)
		lazy val caves = cavemap(x,z)
		val caveLimitReal = Math.min(yCaveMax, height-4).toInt
		Array.tabulate(256)(y => {
			if(cell.cellClosest.x == 0 && cell.cellClosest.z == 0) false
			else {
				val fissureAtten = Math.sqrt(MathUtils.attenuateThrough(yFissureFull, y, yFissureMax))
				val caveAtten = MathUtils.attenuateThrough(Math.max(0, caveLimitReal - 6), y, caveLimitReal)
				y < yFissureMax && fissures(y) > (1 - fissureAtten * 0.06) ||
					y < yCaveMax && caves(y) > (1 - caveAtten * 0.25)
			}
		})
	}
}
