package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.BDomain
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.{CommonBlockTypes, TBlockStateSource}

import scala.collection.mutable

/** A mutable sequence of TBlockStateSources that is usable as a column function in world gen.
	* Structure voids are treated as no-ops, nulls are treated as resets.
	* The column is initialized to all structure voids.
	*/
class CanvasColumn(
	val domain: BDomain,
	/** Function called to update each block state in the worldgen column.
		* Handles the actual update (or not, if desired).
		*/
	val applier: (WorldGenColumn, WBlockState, Int) => Unit,
	private val array: Array[TBlockStateSource]
)
extends TWorldGenColumnFn with mutable.Seq[TBlockStateSource] {
	def this(
		domain: BDomain, length: Int,
		applier: (WorldGenColumn, WBlockState, Int) => Unit = (col,b,y) => col.update(y,b)
	) = this(
		domain,
		applier,
		Array.fill(length)(CommonBlockTypes.STRUCTURE_VOID.asInstanceOf[TBlockStateSource])
	)

	//TODO: We can optimize space usage here.

	final override def apply(col: WorldGenColumn): Unit = for(y <- col.indices) {
		val block: WBlockState = array(y) match {
			case null => null
			case what: TBlockStateSource => what.asWBlockState
		}
		if(block == null) col.reset(y)
		else if(block != CommonBlockTypes.STRUCTURE_VOID) applier(col, block, y)
	}

	final override def length = array.length
	final override def update(idx: Int, elem: TBlockStateSource): Unit = array.update(idx, elem)
	final override def apply(i: Int) = array(i)
	final override def iterator = array.iterator
	def copy = new CanvasColumn(domain, applier, Array.copyOf(array, length))
}
