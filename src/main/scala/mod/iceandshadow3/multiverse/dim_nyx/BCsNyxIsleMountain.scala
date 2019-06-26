package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.compat.block.`type`.{BBlockType, BlockTypeSnow}
import mod.iceandshadow3.gen.Cellmaker.Result
import mod.iceandshadow3.util.MathUtils
import mod.iceandshadow3.util.collect.FixedMap2d

abstract class BCsNyxIsleMountain(noises: NoisesNyx, cells: FixedMap2d[Result])
	extends BCsNyxIsle(noises, cells) {

	def icicleInfrequency = 24

	override def decorate(x: Int, z: Int, in: Array[BBlockType], height: Float, rng: Random): Unit = {
		val hasIcicles = rng.nextInt(icicleInfrequency) == 0
		var doSnow = true
		var doIcicles = hasIcicles
		for (yminus <- -255 to -32) {
			val y = -yminus
			if(doSnow) {
				if (in(y - 1) != null) {
					doSnow = false
					val delta = height - y
					val snowmod = MathUtils.ratioBelow(yFull, y, yThinning)
					in(y) = if (snowmod != 0) {
						val snowdelta = if (smoothsnow) delta else if (delta > 2f / 3) 5d / 7 else 1d / 7
						BlockTypeSnow.fromFloat(snowmod * snowdelta)
					} else null
				}
			} else if(doIcicles) {
				if(in(y) == null) {
					doIcicles = false
					//TODO: Check if it can stay here.
					in(y) = BCsNyxIsle.icicles
				}
			} else return
		}
	}

	override def genHeight(islevalue: Double, x: Int, z: Int) = {
		import noises._
		val ridgescale = Math.sqrt(1-noisemakerRidgeScale(x,z))
		var cratervalue = noisemakerDip(x,z)
		cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
		val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerMountain(x,z)*Math.PI)))/2
		val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerRidge(x,z)*Math.PI)))/2
		var hillvalue = MathUtils.sinelike(1-noisemakerHills(x,z))
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
		retval.toFloat*32
	}
}
