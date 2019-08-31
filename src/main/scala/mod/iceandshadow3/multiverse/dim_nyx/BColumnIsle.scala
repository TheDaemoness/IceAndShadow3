package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.{BlockTypeSnow, CommonBlockTypes, TBlockStateSource}
import mod.iceandshadow3.lib.spatial.{Cells, RandomXZ}
import mod.iceandshadow3.lib.util.MathUtils

abstract class BColumnIsle(seed: Long, x: Int, z: Int, cell: Cells.Result, voidhole: Boolean)
extends BColumn {
	protected def decorate(array: Array[TBlockStateSource], height: Float, r: Random): Unit
	protected def height(): Float
	protected def caves(): Seq[Boolean]

	val islevalue = 1-Cells.distance(cell)

	protected def stoneLower: WBlockState
	protected def stoneUpper: WBlockState

	private val array = {
		import WorldGenNyx._
		val lowerstone = stoneLower
		val upperstone = stoneUpper
		val finalheight = height()
		val colRng = new RandomXZ(seed, 31920, x, z)
		val colNoise = colRng.nextInt(2)
		lazy val caveSeq: Seq[Boolean] = caves()
		val retval = Array.tabulate[TBlockStateSource](255)(y => {
			val delta = finalheight-y
			if(y == 0) navistra
			else if(finalheight < 47) null
			else if(caveSeq(y)) null
			else if(delta > 2) {
				if(y<=10+colNoise) navistra else if(y < finalheight/3) lowerstone else upperstone
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
		decorate(retval, finalheight, colRng)
		retval
	}

	override def apply(i: Int) = {
		val block = array(i)
		if(block == null) blockDefault(i) else block
	}

	override val bedrock = if(voidhole) CommonBlockTypes.AIR else WorldGenNyx.bedrock
}
