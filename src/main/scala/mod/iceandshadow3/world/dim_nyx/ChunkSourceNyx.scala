package mod.iceandshadow3.world.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.block.`type`.{BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.gen.noise.Noise2dCrater
import mod.iceandshadow3.gen.{BChunkSource, BHeightmap}
import mod.iceandshadow3.util.SMath
import mod.iceandshadow3.world.DomainGaia

object ChunkSourceNyx {
	val stonetype = new BlockTypeSimple(DomainGaia.livingstone, 0)
}
class ChunkSourceNyx(noises: NoisesNyx, xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int)
	extends BChunkSource(xFrom, zFrom, xWidth, zWidth)
{
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get
	val heightmap = new BHeightmap(xFrom, zFrom, xWidth, zWidth) {
		override protected def computeHeight(x: Int, z: Int): Float = {
			import noises._
			val isleresults = isleMaker.apply(x,z)
			val islevalue = 1-Noise2dCrater(isleresults)
			val ridgescale = Math.sqrt(1-noisemakerRidgeScale(x,z))
			var cratervalue = noisemakerDip(x,z)
			cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
			val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerMountain(x,z)*Math.PI)))/2
			val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerRidge(x,z)*Math.PI)))/2
			val retval =
				if(islevalue <= 0.15) 0
				else {
					val tuner = if(islevalue <= 0.3) (islevalue-0.2)*10 else 1d
					val base = 2+(islevalue+mountainvalue*tuner)*(1+SMath.sinelike(islevalue))+ridgevalue*tuner+cratervalue
					if(islevalue <= 0.2) base*SMath.sinelike((islevalue-0.15)*20)
					else base
				}
			retval.toFloat
		}
	}
	override def getBlock(x: Int, y: Int, z: Int) = {
		val finalheight = heightmap(x-xFrom,z-zFrom)*32
		val delta = finalheight-y
		if(delta > 2) ChunkSourceNyx.stonetype
		else if(finalheight < 68) BlockTypeSimple.AIR
		else if(finalheight > 192) BlockTypeSimple.AIR //TODO: Aurora air.
		else if(delta > 1) {
			if(finalheight <= 160) BlockTypeSnow.SNOWS.last
			else BlockTypeSnow.fromFloat((192-finalheight)/32)
		} else if(delta > 0) {
			val snowdelta = if(smoothsnow) delta else if(delta > 2f/3) 5d/7 else 1d/7
			val snowmod = Math.max(Math.min(1, (160-finalheight)/16), 0)
			if(snowmod != 0) BlockTypeSnow.fromFloat(snowmod*snowdelta) else BlockTypeSimple.AIR
		} else BlockTypeSimple.AIR
	}
}
