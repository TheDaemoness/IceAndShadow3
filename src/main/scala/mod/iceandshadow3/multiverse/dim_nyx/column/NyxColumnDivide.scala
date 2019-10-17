package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.gen.WorldGenColumn
import mod.iceandshadow3.lib.spatial.Cells

class NyxColumnDivide(cell: Cells.Result)
extends BNyxColumn(cell, false) {
	override val height = 0f
	override def apply(discard: WorldGenColumn): Unit = () //TODO: Worldgen in the divides.
}
