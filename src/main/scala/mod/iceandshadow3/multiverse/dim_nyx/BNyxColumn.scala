package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.gen.BWorldGenColumnFn
import mod.iceandshadow3.lib.spatial.Cells
import mod.iceandshadow3.multiverse.DomainGaia

abstract class BNyxColumn(cell: Cells.Result) extends BWorldGenColumnFn(DomainGaia) {
	def height: Float
	val islevalue = 1-Cells.distance(cell)
}
