package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.gen.TWorldGenColumnFn
import mod.iceandshadow3.lib.spatial.Cells
import mod.iceandshadow3.multiverse.{DomainGaia, DomainNyx}

abstract class BNyxColumn(cell: Cells.Result, val isLand: Boolean) extends TWorldGenColumnFn {
	def height: Float
	def hasCaveAt(y: Int): Boolean
	val islevalue = 1-Cells.distance(cell)
	override def domain = DomainGaia
}
