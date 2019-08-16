package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.util.MathUtils

abstract class BColumnMountain(x: Int, z: Int, val region: RegionInterpret, voidhole: Boolean)
extends BColumn(region.noises.seed, x, z, region.islemap(x, z), voidhole) {

	override protected def height() = {
		val ridgescale = Math.sqrt(1-region.scaleMap(x,z))
		var cratervalue = region.craterMap(x,z)
		cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
		val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*region.mountainMap(x,z)*Math.PI)))/2
		val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*region.ridgeMap(x,z)*Math.PI)))/2
		var hillvalue = MathUtils.sinelike(1-region.hillMap(x,z))
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
