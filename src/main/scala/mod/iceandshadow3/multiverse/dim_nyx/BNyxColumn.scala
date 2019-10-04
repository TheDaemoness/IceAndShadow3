package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.gen.WorldGenColumn
import mod.iceandshadow3.lib.spatial.Cells

abstract class BNyxColumn(cell: Cells.Result) extends (WorldGenColumn => Unit) {
	def height: Float
	val islevalue = 1-Cells.distance(cell)
}
