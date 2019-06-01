package mod.iceandshadow3.world.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.block.BBlockType
import mod.iceandshadow3.compat.block.`type`.{BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.gen.{BWorldSource, Cellmaker, ColumnRandom, Noise2dCrater}
import mod.iceandshadow3.spatial.XZPair
import mod.iceandshadow3.util.SMath
import mod.iceandshadow3.world.DomainGaia

class WorldSourceNyx(seed: Long) extends BWorldSource {
	val stonetype = new BlockTypeSimple(DomainGaia.livingstone, 0)
	val isleMaker = new Cellmaker(seed, 9967, 1200) {
		override def cellToPoint(cell: XZPair, rng: ColumnRandom) = {
			if(cell.x == 0 && cell.z == 0) XZPair(0,0)
			else super.cellToPoint(cell, rng)
		}
	}
	val noisemakerDip = new Noise2dCrater(seed, 1928, 60)
	val noisemakerMountain = new Noise2dCrater(seed, 3092, 150)
	val noisemakerRidgeScale = new Noise2dCrater(seed, 4815, 420)
	val noisemakerRidge = new Noise2dCrater(seed, 6872, 230)
	override def getColumn(x: Int, z: Int): Iterator[BBlockType] = {
		val isleresults = isleMaker.apply(x,z)
		val islevalue = 1-Noise2dCrater(isleresults)
		val ridgescale = Math.sqrt(1-noisemakerRidgeScale(x,z))
		var cratervalue = noisemakerDip(x,z)
		cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
		val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerMountain(x,z)*Math.PI)))/2
		val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerRidge(x,z)*Math.PI)))/2
		val height = {
			if(islevalue <= 0.15) 0
			else {
				val tuner = if(islevalue <= 0.3) (islevalue-0.2)*10 else 1d
				val base = 2+(islevalue+mountainvalue*tuner)*(1+SMath.sinelike(islevalue))+ridgevalue*tuner+cratervalue
				if(islevalue <= 0.2) base*SMath.sinelike((islevalue-0.15)*20)
				else base
			}
		}
		new Iterator[BBlockType]() {
			var y = 0
			val finalheight = height*32
			val smoothsnow = IaS3.getCfgServer.smooth_snow.get
			override def hasNext = y < 256

			override def next() = {
				val delta = finalheight-y
				y += 1
				if(delta > 2) stonetype
				else if(finalheight-cratervalue*8 < 64) BlockTypeSimple.AIR
				else if(finalheight > 192) BlockTypeSimple.AIR //TODO: Aurora air.
				else if(delta > 1) {
					if(finalheight <= 160) BlockTypeSnow.SNOWS.last
					else BlockTypeSnow.fromFloat((192-finalheight)/32)
				} else if(delta > 0) {
					val snowdelta = if(smoothsnow) delta else if(delta > 2d/3) 5d/7 else 1d/7
					val snowmod = Math.max(Math.min(1, (160-finalheight)/16), 0)
					if(snowmod != 0) BlockTypeSnow.fromFloat(snowmod*snowdelta) else BlockTypeSimple.AIR
				} else BlockTypeSimple.AIR
			}

		}
	}
}
