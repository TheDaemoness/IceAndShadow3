package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.gen.{BWorldGenLayerTerrain, WorldGenColumn}
import mod.iceandshadow3.lib.spatial.Cells
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx.navistra

abstract class BNyxColumnIsle(cell: Cells.Result)
extends BNyxColumn(cell, true) {
	protected def genHeight(): Float
	override val height = genHeight()

	protected def caves(): Seq[Boolean]
	protected def surface(y: Int): WBlockState

	protected def stoneLower: WBlockState
	protected def stoneUpper: WBlockState

	private lazy val caveSeq: Seq[Boolean] = caves()
	protected val lowerstone = stoneLower
	protected val upperstone = stoneUpper

	final override def hasCaveAt(y: Int): Boolean = y < caveSeq.length && caveSeq(y)

	private def change(out: WorldGenColumn, y: Int, state: WBlockState): Unit = {
		if(!caveSeq(y)) out.update(y, state)
	}

	override def apply(out: WorldGenColumn): Unit = {
		out.update(BWorldGenLayerTerrain.varHeight, height)
		if(height < 48) return
		val baseNavistra = WorldGenNyx.yExousia + WorldGenNyx.yNavistraExtra
		for(y <- 1 to baseNavistra) change(out, y, navistra)
		change(out, baseNavistra+1, if(out.rng.nextBoolean()) navistra else lowerstone)
		val stoneChange = (3*height/8).toInt
		for(y <- baseNavistra+2 until stoneChange) change(out, y, lowerstone)
		val finalheightInt = height.toInt
		for(y <- stoneChange until finalheightInt) change(out, y, upperstone)
		change(out, finalheightInt, surface(finalheightInt))
	}
}
