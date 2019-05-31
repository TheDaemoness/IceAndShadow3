package mod.iceandshadow3.gen

import mod.iceandshadow3.compat.block.BBlockType

abstract class BWorldSource {
	def getColumn(x: Int, z: Int): Iterator[BBlockType]
}
