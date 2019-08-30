package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource

/** A 1x256x1 world generation cell.
	* As a seq, contains all of the blocks except the lowest block layer.
	*/
abstract class BWorldGenColumn extends Seq[TBlockStateSource] {
	final override def length = 255

	override def iterator: Iterator[TBlockStateSource] = new Iterator[TBlockStateSource] {
		var i = -1

		override def hasNext = i < 254

		override def next() = {
			i += 1
			apply(i)
		}
	}

	def bedrock(): TBlockStateSource
}
