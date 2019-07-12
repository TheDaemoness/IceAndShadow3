package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.block.`type`.BBlockType

abstract class BRegionRef(val xFrom: Int, val zFrom: Int, val xMax: Int, val zMax: Int) {
	def apply(xBlock: Int, yBlock: Int, zBlock: Int): WBlockRef
	def update(xBlock: Int, yBlock: Int, zBlock: Int, newtype: BBlockType): Unit
}
