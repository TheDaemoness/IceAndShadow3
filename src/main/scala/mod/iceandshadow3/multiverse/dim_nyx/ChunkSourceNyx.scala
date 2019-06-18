package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.block.`type`.{BBlockType, BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.gen.{BChunkSource, Cellmaker, Cellmaker3d, TerrainMap}
import mod.iceandshadow3.spatial.RandomXZ
import mod.iceandshadow3.util.MathUtils
import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
import mod.iceandshadow3.multiverse.DomainNyx

object ChunkSourceNyx {
	val stone = new BlockTypeSimple(livingstone, 0)
	val navistra = new BlockTypeSimple(navistra_stone, 0)
	val bedrock = new BlockTypeSimple(navistra_bedrock, 0)
	val icicles = new BlockTypeSimple(DomainNyx.Blocks.icicles, 0)
	val exousia = new BlockTypeSimple(DomainNyx.Blocks.exousia, 0)
}
class ChunkSourceNyx(noises: NoisesNyx, xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int)
	extends BChunkSource(xFrom, zFrom, xWidth, zWidth)
{
	val yBald = 188
	val yThinning = 176
	val yFull = 168
	val yFissureFull = 148
	val yFissureMax = 172
	val yCaveMax = 148
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get
	val icicleInfrequency = 24

	val heightmap = new TerrainMap[Float](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			import noises._
			val isleresults = isleMaker.apply(x,z)
			val islevalue = 1-Cellmaker.distance(isleresults)
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
	})

	private def make3dMap(a: Cellmaker3d, b: Cellmaker3d, limit: Int) = {
		val resultsA = a(xFrom, 0, zFrom, xFrom+xWidth, limit, zFrom+zWidth)
		val resultsB = b(xFrom, 0, zFrom, xFrom+xWidth, limit, zFrom+zWidth)
		new TerrainMap[Array[Float]](xFrom, zFrom, xWidth, zWidth, (x, z) => {
			Array.tabulate[Float](limit)(y => (
				Cellmaker.distance(resultsA(x-xFrom)(y)(z-zFrom)) *
					Cellmaker.distance(resultsB(x-xFrom)(y)(z-zFrom))
				).toFloat
			)
		})
	}

	lazy val fissuremap = make3dMap(noises.fissuremakerA, noises.fissuremakerB, yFissureMax)
	lazy val cavemap = make3dMap(noises.cavemakerA, noises.cavemakerB, yCaveMax)

	def getAir(y: Int)= {
		if(y <= 9) ChunkSourceNyx.exousia
		else null
	}

	override def getColumn(x: Int, z: Int): Array[BBlockType] = {
		val finalheight = heightmap(x,z)*32
		lazy val fissures = fissuremap(x,z)
		lazy val caves = cavemap(x,z)
		val colRng = new RandomXZ(noises.seed, 31920, x, z)
		val colNoise = colRng.nextInt(2)
		val noIcicles = colRng.nextInt(icicleInfrequency)
		val retval = Array.tabulate[BBlockType](256)(y => {
			val delta = finalheight-y
			val fissureAtten = Math.sqrt(MathUtils.attenuateThrough(yFissureFull, y, yFissureMax))
			val caveLimitReal = Math.min(yCaveMax,finalheight-4).toInt
			val caveAtten = MathUtils.attenuateThrough(Math.max(0, caveLimitReal-6), y, caveLimitReal)
			if(y == 0) ChunkSourceNyx.bedrock
			else if(y == 1) ChunkSourceNyx.navistra
			else if(finalheight < 48) getAir(y)
			else if(y < yFissureMax && fissures(y) > (1-fissureAtten*0.05)) getAir(y)
			else if(y < yCaveMax && caves(y) > (1-caveAtten*0.25)) getAir(y)
			else if(delta > 2) {
				if(y<=11+colNoise && (y <= 1+colNoise || finalheight <= 64 || fissures(y) > 0.4)) {
					ChunkSourceNyx.navistra
				} else ChunkSourceNyx.stone
			}
			else if(y > yBald) null
			else if(delta > 1) {
				if(finalheight <= yThinning) BlockTypeSnow.SNOWS.last
				else {
					val snowmod = MathUtils.attenuateThrough(yThinning, y, yBald)
					if(snowmod != 0) BlockTypeSnow.fromFloat(snowmod) else null
				}
			} else null
		})
		//TODO: Structure gen.
		var doSnow = true
		var doIcicles = true
		for (yminus <- -255 to -32) {
			val y = -yminus
			if(doSnow) {
				if (retval(y - 1) != null) {
					doSnow = false
					val delta = finalheight - y
					val snowmod = MathUtils.attenuateThrough(yFull, y, yThinning)
					retval(y) = if (snowmod != 0) {
						val snowdelta = if (smoothsnow) delta else if (delta > 2f / 3) 5d / 7 else 1d / 7
						BlockTypeSnow.fromFloat(snowmod * snowdelta)
					} else null
				}
			} else if(doIcicles) {
				if(retval(y) == null) {
					doIcicles = false
					if(noIcicles == 0) {
						//TODO: Check if it can stay here.
						retval(y) = ChunkSourceNyx.icicles
					}
				}
			} else return retval
		}
		retval
	}
}
