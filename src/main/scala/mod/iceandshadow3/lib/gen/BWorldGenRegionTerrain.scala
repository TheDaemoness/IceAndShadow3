package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.data.BVar
import mod.iceandshadow3.lib.spatial.{IPosColumn, PairXZ}

/** Data for an aligned 32x256x32 world generation cell.
	*/
object BWorldGenRegionTerrain {
	final val width = 32 //A value of 2^n where n >= 5.
	def coord(blockCoord: Int): Int = (blockCoord + 16) >> 5
	def toEdge(regionCoord: Int): Int = (regionCoord << 5) - 16
	val varHeight = new BVar[Float]("height") {
		override def defaultVal = 0f
	}
	val COLUMN_FN_NO_OP: WorldGenColumn => Unit = _ => ()
}
abstract class BWorldGenRegionTerrain(scaled: PairXZ) extends BWorldGenRegion (
	BWorldGenRegionTerrain.toEdge(scaled.x),
	BWorldGenRegionTerrain.toEdge(scaled.z),
	BWorldGenRegionTerrain.width-1
) {
	protected def columnFn(where: IPosColumn): WorldGenColumn => Unit
	override def apply(out: WorldGenColumn): Unit = columnFn(out)(out)
}
