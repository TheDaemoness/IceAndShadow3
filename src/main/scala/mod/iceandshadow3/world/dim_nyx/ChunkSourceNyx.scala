package mod.iceandshadow3.world.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.block.BBlockType
import mod.iceandshadow3.compat.block.`type`.{BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.gen.{BChunkSource, TerrainMap, Cellmaker}
import mod.iceandshadow3.util.SMath
import mod.iceandshadow3.world.DomainGaia

object ChunkSourceNyx {
	val stonetype = new BlockTypeSimple(DomainGaia.livingstone, 0)
}
class ChunkSourceNyx(noises: NoisesNyx, xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int)
	extends BChunkSource(xFrom, zFrom, xWidth, zWidth)
{
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get
	val heightmap = new TerrainMap[Float](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			import noises._
			val isleresults = isleMaker.apply(x,z)
			val islevalue = 1-Cellmaker.distance(isleresults)
			val ridgescale = Math.sqrt(1-noisemakerRidgeScale(x,z)(0))
			var cratervalue = noisemakerDip(x,z)(0)
			cratervalue *= Math.cbrt(cratervalue)/(4-ridgescale)
			val mountainvalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerMountain(x,z)(0)*Math.PI)))/2
			val ridgevalue = (1-Math.cbrt(Math.cos(ridgescale*noisemakerRidge(x,z)(0)*Math.PI)))/2
			var hillvalue = SMath.sinelike(1-noisemakerHills(x,z)(0))
			hillvalue *= hillvalue
			val retval =
				if(islevalue <= 0.15) 0
				else {
					val tuner = if(islevalue <= 0.3) (islevalue-0.2)*10 else 1d
					val totalmountainvalue = (ridgevalue+mountainvalue)*tuner*(1+islevalue)+hillvalue
					val base = 1.5+SMath.sinelike(islevalue)+totalmountainvalue+cratervalue
					if(islevalue <= 0.2) base*SMath.sinelike((islevalue-0.15)*20)
					else base
				}
			retval.toFloat
	})

	val cavemap = {
		val ylimit = 180
		val caveresultsA = noises.cavemakerA(xFrom, 0, zFrom, xFrom+xWidth, ylimit, zFrom+zWidth)
		val caveresultsB = noises.cavemakerB(xFrom, 0, zFrom, xFrom+xWidth, ylimit, zFrom+zWidth)
		new TerrainMap[Array[Float]](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			Array.tabulate[Float](ylimit)(y => (
					Cellmaker.distance(caveresultsA(x-xFrom)(y)(z-zFrom)) *
					Cellmaker.distance(caveresultsB(x-xFrom)(y)(z-zFrom))
				).toFloat
			)
		})
	}

	override def getColumn(x: Int, z: Int): Array[BBlockType] = {
		val finalheight = heightmap(x,z)*32
		val caves = cavemap(x,z)
		val retval = Array.tabulate[BBlockType](256)(y => {
			val delta = finalheight-y
			if(finalheight < 48) null
			else if(y < 180 && caves(y) > (1-SMath.attenuateBetween(156, y, 172)*0.25)) null
			else if(delta > 2) ChunkSourceNyx.stonetype
			else if(y > 192) null
			else if(delta > 1) {
				if(finalheight <= 160) BlockTypeSnow.SNOWS.last
				else {
					val snowmod = SMath.attenuateBetween(160, y, 192)
					if(snowmod != 0) BlockTypeSnow.fromFloat(snowmod) else null
				}
			} else null
		})
		//TODO: Structure gen.
			for (yminus <- -255 to -24) {
				val y = -yminus
				if (retval(y - 1) != null) {
					val delta = finalheight - y
					val snowmod = SMath.attenuateBetween(144, y, 160)
					retval(y) = if (snowmod != 0) {
						val snowdelta = if (smoothsnow) delta else if (delta > 2f / 3) 5d / 7 else 1d / 7
						BlockTypeSnow.fromFloat(snowmod * snowdelta)
					} else null
					return retval
				}
			}
		retval
	}
}
