package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.spatial.IPosColumn

import scala.collection.mutable

/** A two-phase constructed array of WBlockStates with fixed length. */
final class WorldGenColumn private[gen](
	private[gen] var x: Int,
	private[gen] var z: Int,
	defaultBlock: Int => WBlockState
) extends IPosColumn with mutable.Seq[WBlockState] {
	override def length = 256
	private var array: Array[WBlockState] = _
	/** Call before initial use, lest you suffer NPEs. */
	private[gen] def reset(): Unit = {
		array = Array.tabulate(length)(defaultBlock)
	}

	override def xBlock = x
	override def zBlock = z

	override def apply(y: Int) = array(y)
	override def update(y: Int, elem: WBlockState): Unit = array.update(y, elem)
	def update(y: Int, elem: TBlockStateSource): Unit = update(y, elem.asWBlockState)
	def reset(y: Int): Unit = array.update(y, defaultBlock(y))
	override def iterator = array.iterator
}
