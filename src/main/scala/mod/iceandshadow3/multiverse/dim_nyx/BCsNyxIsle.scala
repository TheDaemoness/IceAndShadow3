package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.compat.block.`type`.{BBlockType, BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.gen.Cellmaker.Result
import mod.iceandshadow3.gen.{BChunkSource, Cellmaker}
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.spatial.RandomXZ
import mod.iceandshadow3.util.MathUtils
import mod.iceandshadow3.util.collect.FixedMap2d

object BCsNyxIsle {
	import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
	val stone = new BlockTypeSimple(livingstone, 0)
	val navistra = new BlockTypeSimple(navistra_stone, 0)
	val bedrock = new BlockTypeSimple(navistra_bedrock, 0)
	val icicles = new BlockTypeSimple(DomainNyx.Blocks.icicles, 0)
	val exousia = new BlockTypeSimple(DomainNyx.Blocks.exousia, 0)
}
abstract class BCsNyxIsle(noises: NoisesNyx, protected val cells: FixedMap2d[Result])
	extends BChunkSource (cells.xFrom, cells.zFrom, cells.xWidth, cells.zWidth)
{
	import BCsNyxIsle._
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get
	val yBald = 188
	val yThinning = 176
	val yFull = 168

	def genHeight(islevalue: Double, x: Int, z: Int): Float
	def caveGen(height: Float, x: Int, z: Int): Seq[Boolean]
	def decorate(x: Int, z: Int, in: Array[BBlockType], height: Float, rng: Random): Unit

	override def getColumn(x: Int, z: Int): Array[BBlockType] = {
		val cell = cells(x, z)
		val islelevel = 1-Cellmaker.distance(cell)
		val finalheight = genHeight(islelevel, x, z)
		val colRng = new RandomXZ(noises.seed, 31920, x, z)
		val colNoise = colRng.nextInt(2)
		lazy val caveSeq: Seq[Boolean] = caveGen(finalheight, x, z)
		val retval = Array.tabulate[BBlockType](256)(y => {
			val delta = finalheight-y
			if(y == 0) bedrock
			else if(y == 1) navistra
			else if(finalheight < 48) null
			else if(caveSeq(y)) null
			else if(delta > 2) {
				if(y<=11+colNoise) navistra else stone
			}
			else if(y > yBald) null
			else if(delta > 1) {
				if(finalheight <= yThinning) BlockTypeSnow.SNOWS.last
				else {
					val snowmod = MathUtils.ratioBelow(yThinning, y, yBald)
					if(snowmod != 0) BlockTypeSnow.fromFloat(snowmod) else null
				}
			} else null
		})
		//TODO: Structure gen.
		decorate(x, z, retval, finalheight, colRng)
		retval
	}
}
