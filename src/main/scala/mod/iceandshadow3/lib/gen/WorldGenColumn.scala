package mod.iceandshadow3.lib.gen

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.data.BVar
import mod.iceandshadow3.lib.spatial.IPosColumn

import scala.collection.mutable

/** A two-phase constructed array of WBlockStates with fixed length. */
final class WorldGenColumn private[gen](
	private[gen] var x: Int,
	private[gen] var z: Int,
	val rng: java.util.Random,
	defaultBlock: Int => WBlockState
) extends IPosColumn with mutable.Seq[WBlockState] {
	override def length = 256
	private var array: Array[WBlockState] = _
	private var metadata: mutable.HashMap[BVar[_], Any] = _
	/** Call before initial use, lest you suffer NPEs. */
	private[gen] def reset(): Unit = {
		array = Array.tabulate(length)(defaultBlock)
		metadata = new mutable.HashMap[BVar[_], Any]
	}

	override def xBlock = x
	override def zBlock = z

	override def apply(y: Int) = array(y)
	override def update(y: Int, elem: WBlockState): Unit = if(elem != null) array.update(y, elem)
	def reset(y: Int): Unit = array.update(y, defaultBlock(y))
	override def iterator = array.iterator

	def apply[T](what: BVar[T]): T = metadata.getOrElseUpdate(what, what.defaultVal).asInstanceOf[T]
	def update[T](what: BVar[T], value: T): Unit = metadata.update(what, value)
}
