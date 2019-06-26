package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.gen.Cellmaker.Result
import mod.iceandshadow3.gen.{BChunkSourceCelled, BWorldSource, Cellmaker2d}
import mod.iceandshadow3.multiverse.dim_nyx.BCsNyxIsle.exousia
import mod.iceandshadow3.spatial.PairXZ
import mod.iceandshadow3.util.collect.FixedMap2d

class WorldSourceNyx(seed: Long) extends BWorldSource {
	private val noises = new NoisesNyx(seed)
	private val isleMaker = new Cellmaker2d(seed, 9967, 1200) {
		override def cellToPoint(xCell: Int, zCell: Int, rng: Random) = {
			if (xCell == 0 && zCell == 0) PairXZ(0, 0)
			else super.cellToPoint(xCell, zCell, rng)
		}
	}
	class ChunkSourceNyx(cells: FixedMap2d[Result]) extends BChunkSourceCelled(cells) {
		private lazy val central = new CsNyxIsleCentral(noises, cells)
		private lazy val mountains = new CsNyxIsleStandard(noises, cells)

		override def cellToSource(cell: Result) = {
			val c = cell.cellClosest
			if(c.x == 0 && c.z == 0) central else mountains
		}

		override def getDefault(y: Int)= {
			if(y <= 9) exousia
			else BlockTypeSimple.AIR
		}
	}
	override def getTerrainChunk(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) = {
		val cells = isleMaker.apply(xFrom, zFrom, xWidth, zWidth)
		new ChunkSourceNyx(cells)
	}
}
