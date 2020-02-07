package mod.iceandshadow3.multiverse.dim_nyx.column

import mod.iceandshadow3.lib.gen.WorldGenColumn
import mod.iceandshadow3.lib.spatial.Cells

class ColumnFnNyxDivide(cell: Cells.Result)
extends ColumnFnNyx(cell, false) {
	override def hasCaveAt(y: Int) = true
	override val height = 0f
	override def apply(discard: WorldGenColumn): Unit = () //TODO: Worldgen in the divides.
}
