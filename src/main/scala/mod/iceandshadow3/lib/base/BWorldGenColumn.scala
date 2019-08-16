package mod.iceandshadow3.lib.base

import mod.iceandshadow3.lib.compat.block.`type`.BBlockType

/** A 1x256x1 world generation cell.
	* As a seq, contains all of the blocks except the lowest block layer.
	*/
abstract class BWorldGenColumn extends Seq[BBlockType] {
	final override def length = 255;

	override def iterator: Iterator[BBlockType] = new Iterator[BBlockType] {
		var i = -1;

		override def hasNext = i < 254;

		override def next() = {
			i += 1;
			apply(i);
		}
	}

	def bedrock(): BBlockType
}
