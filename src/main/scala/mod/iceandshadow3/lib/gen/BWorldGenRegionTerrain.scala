package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.data.BVar
import mod.iceandshadow3.lib.spatial.{IPosColumn, PairXZ}
import mod.iceandshadow3.lib.util.collect.FixedMap2d

/** Data for an aligned 32x256x32 world generation cell.
	*/
object BWorldGenRegionTerrain {
	final val width = 32 //A value of 2^n where n >= 5.
	def coord(blockCoord: Int): Int = (blockCoord + 16) >> 5
	def toEdge(regionCoord: Int): Int = regionCoord*32 - 16
	val varHeight = new BVar[Float]("height") {
		override def defaultVal = 0f
	}
}
abstract class BWorldGenRegionTerrain(scaled: PairXZ)
extends BWorldGenRegion (
	BWorldGenRegionTerrain.toEdge(scaled.x),
	BWorldGenRegionTerrain.toEdge(scaled.z),
	BWorldGenRegionTerrain.width-1
) {
	protected def newGenerator(): (Int, Int) => F
	private val map = new FixedMap2d[F](
		xFrom, zFrom,
		BWorldGenRegionTerrain.width, BWorldGenRegionTerrain.width,
		newGenerator()
	)
	final override def apply(where: IPosColumn) = map(where.xBlock, where.zBlock)
}
