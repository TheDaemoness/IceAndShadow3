package mod.iceandshadow3.gen

import mod.iceandshadow3.gen.Cellmaker.Result
import mod.iceandshadow3.util.collect.FixedMap2d

/** Mixes (but does not blend) columns from multiple chunk sources based on cellmaker results.
	* Useful for districts or biomes.
	*/
abstract class BChunkSourceCelled(cells: FixedMap2d[Result])
extends BChunkSource(cells.xFrom, cells.zFrom, cells.xWidth, cells.zWidth) {
	def cellToSource(cell: Result): BChunkSource

	override def getColumn(x: Int, z: Int) = {
		cellToSource(cells(x,z)).getColumn(x, z)
	}
}
