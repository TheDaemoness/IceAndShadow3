package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.{CommonBlockTypes, TBlockStateSource}

import scala.collection.mutable

/** A mutable sequence of TBlockStateSources that is usable as a column function in world gen.
	* Structure voids are treated as no-ops, nulls are treated as resets.
	* The column is initialized to all structure voids.
	*/
class CanvasColumn(val domain: BDomain, val length: Int)
extends TWorldGenColumnFn with mutable.Seq[TBlockStateSource] {
	private val array = Array.fill(length)(CommonBlockTypes.STRUCTURE_VOID.asInstanceOf[TBlockStateSource])
	override def apply(col: WorldGenColumn): Unit = for(y <- col.indices) {
		val block: WBlockState = array(y) match {
			case null => null
			case what: TBlockStateSource => what.asWBlockState
		}
		if(block == null) col.reset(y)
		else if(block != CommonBlockTypes.STRUCTURE_VOID) col.update(y, block)
	}
	override def update(idx: Int, elem: TBlockStateSource): Unit = array.update(idx, elem)
	override def apply(i: Int) = array(i)
	override def iterator = array.iterator
}
