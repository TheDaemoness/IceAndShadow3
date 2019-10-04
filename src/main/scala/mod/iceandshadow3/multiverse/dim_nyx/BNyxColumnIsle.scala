package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.gen.{BWorldGenRegionTerrain, WorldGenColumn}
import mod.iceandshadow3.lib.spatial.Cells
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx.navistra

abstract class BNyxColumnIsle(seed: Long, x: Int, z: Int, cell: Cells.Result)
extends (WorldGenColumn => Unit) {
	protected def height(): Float
	protected def caves(): Seq[Boolean]
	protected def surface(y: Int): WBlockState

	val islevalue = 1-Cells.distance(cell)

	protected def stoneLower: WBlockState
	protected def stoneUpper: WBlockState

	val finalheight = height()

	protected lazy val caveSeq: Seq[Boolean] = caves()
	protected val lowerstone = stoneLower
	protected val upperstone = stoneUpper

	private def change(out: WorldGenColumn, y: Int, state: WBlockState): Unit = {
		if(!caveSeq(y)) out.update(y, state)
	}

	override def apply(out: WorldGenColumn): Unit = {
		out.update(BWorldGenRegionTerrain.varHeight, finalheight)
		if(finalheight < 48) return
		val baseNavistra = WorldGenNyx.yExousia + WorldGenNyx.yNavistraExtra
		for(y <- 1 to baseNavistra) change(out, y, navistra)
		change(out, baseNavistra+1, if(out.rng.nextBoolean()) navistra else lowerstone)
		val stoneChange = (finalheight/3).toInt
		for(y <- baseNavistra+2 until stoneChange) change(out, y, lowerstone)
		val finalheightInt = finalheight.toInt
		for(y <- stoneChange until finalheightInt) change(out, y, upperstone)
		change(out, finalheightInt, surface(finalheightInt))
	}
}
