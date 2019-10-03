package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.block.{WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.spatial.{BWorldRegion, IPosBlock}

abstract class BWorldRegionRef(xFrom: Int, zFrom: Int, val xMax: Int, val zMax: Int)
extends BWorldRegion(xFrom, zFrom) {
	override val xWidth = xMax - xFrom
	override val zWidth = zMax - zFrom

	def apply(where: IPosBlock): WBlockRef
	def state(where: IPosBlock): WBlockState = apply(where)
	def update(where: IPosBlock, newtype: TBlockStateSource): Unit
}
