package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.base.BWorldGenColumn
import mod.iceandshadow3.lib.compat.block.`type`.{BBlockType, BlockTypeSimple, BlockTypeSnow}
import mod.iceandshadow3.lib.spatial.{Cells, RandomXZ}
import mod.iceandshadow3.lib.util.MathUtils

abstract class BColumn(seed: Long, x: Int, z: Int, cell: Cells.Result, voidhole: Boolean)
extends BWorldGenColumn {
	protected def decorate(array: Array[BBlockType], height: Float, r: Random): Unit
	protected def height(): Float
	protected def caves(): Seq[Boolean]

	val islevalue = 1-Cells.distance(cell)

	protected def stoneLower: BlockTypeSimple
	protected def stoneUpper: BlockTypeSimple

	private val array = {
		import WorldGenNyx._
		val lowerstone = stoneLower
		val upperstone = stoneUpper
		val finalheight = height()
		val colRng = new RandomXZ(seed, 31920, x, z)
		val colNoise = colRng.nextInt(2)
		lazy val caveSeq: Seq[Boolean] = caves()
		val retval = Array.tabulate[BBlockType](255)(y => {
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

	protected def blockDefault(i: Int): BBlockType =
		if (i <= 8) WorldGenNyx.exousia else BlockTypeSimple.AIR
	override def apply(i: Int) = {
		val block = array(i)
		if(block == null) blockDefault(i) else block
	}

	override val bedrock = if(voidhole) BlockTypeSimple.AIR else WorldGenNyx.bedrock
}
