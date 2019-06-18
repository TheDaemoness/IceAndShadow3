package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.compat.block.`type`.{BBlockType, BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.gen.{BChunkSource, Cellmaker}
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.spatial.RandomXZ
import mod.iceandshadow3.util.MathUtils

object BChunksNyx {
	import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
	val stone = new BlockTypeSimple(livingstone, 0)
	val navistra = new BlockTypeSimple(navistra_stone, 0)
	val bedrock = new BlockTypeSimple(navistra_bedrock, 0)
	val icicles = new BlockTypeSimple(DomainNyx.Blocks.icicles, 0)
	val exousia = new BlockTypeSimple(DomainNyx.Blocks.exousia, 0)
	val yBald = 188
	val yThinning = 176
	val yFull = 168
}
abstract class BChunksNyx(noises: NoisesNyx, xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int)
	extends BChunkSource (xFrom, zFrom, xWidth, zWidth)
{
	import BChunksNyx._
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get
	val cells = noises.isleMaker.apply(xFrom, zFrom, xFrom+xWidth, zFrom+zWidth)
	def icicleInfrequency = 24

	override def getDefault(y: Int)= {
		if(y <= 9) exousia
		else BlockTypeSimple.AIR
	}

	def genHeight(cell: Cellmaker.Result, x: Int, z: Int): Float
	def caveGen(cell: Cellmaker.Result, height: Float, x: Int, z: Int): Seq[Boolean]

	override def getColumn(x: Int, z: Int): Array[BBlockType] = {
		val cell = cells(x-xFrom)(z-zFrom)
		val finalheight = genHeight(cell, x, z)*32
		val colRng = new RandomXZ(noises.seed, 31920, x, z)
		val colNoise = colRng.nextInt(2)
		lazy val caveSeq: Seq[Boolean] = caveGen(cell, finalheight, x, z)
		val hasIcicles = colRng.nextInt(icicleInfrequency) == 0
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
					val snowmod = MathUtils.attenuateThrough(yThinning, y, yBald)
					if(snowmod != 0) BlockTypeSnow.fromFloat(snowmod) else null
				}
			} else null
		})
		//TODO: Structure gen.
		var doSnow = true
		var doIcicles = hasIcicles
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
					//TODO: Check if it can stay here.
					retval(y) = icicles
				}
			} else return retval
		}
		retval
	}
}
