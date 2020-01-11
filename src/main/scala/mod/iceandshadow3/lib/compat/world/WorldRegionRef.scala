package mod.iceandshadow3.lib.compat.world

import mod.iceandshadow3.lib.compat.block.{WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.spatial.{WorldRegion, IPosBlock}

abstract class WorldRegionRef(xFrom: Int, zFrom: Int, xMax: Int, zMax: Int)
extends WorldRegion(xFrom, zFrom, xMax, zMax) {
	def isIn(where: IPosBlock) =
		where.xBlock >= xFrom && where.xBlock <= xMax &&
		where.zBlock >= zFrom && where.zBlock <= zMax
	def apply(where: IPosBlock): WBlockRef
	def state(where: IPosBlock): WBlockState = apply(where)
	def update(where: IPosBlock, newtype: TBlockStateSource): Unit
}
