package mod.iceandshadow3.multiverse.dim_nyx.structure

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.gen.{BWorldGenLayerStructuresSparse, BWorldGenStructureTypeSimple, TWorldGenColumnFn, TWorldGenLayer, WorldGenColumn}
import mod.iceandshadow3.multiverse.{DomainGaia, DomainNyx}
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx
import mod.iceandshadow3.multiverse.dim_nyx.column.BNyxColumn

object NyxLayerCrystals {
	val nifelhium = DomainNyx.Blocks.nifelhiumCrystal.asWBlockState(0)
	val variance = 16
	val margin = 6
	def block(height: Int): WBlockState = if(height > WorldGenNyx.yBald) nifelhium else CommonBlockTypes.AIR
	// ^ TODO: Different "crystal" at snow level.

	val noOp = TWorldGenColumnFn.noOp(DomainGaia)
	class Column(height: Int) extends TWorldGenColumnFn {
		protected val block = NyxLayerCrystals.block(height)
		override def domain = block.asWBlockState.getDomain
		override def apply(col: WorldGenColumn): Unit =
			if(height > WorldGenNyx.yExousia+2) {
				col.update(height, block.asWBlockState)
			}
	}
	class StructureType extends BWorldGenStructureTypeSimple[TWorldGenColumnFn, BNyxColumn](1, 1) {
		override def columnAt(xRela: Int, zRela: Int, parent: BNyxColumn) = new Column(Math.min(254, parent.height.toInt))
	}
}
class NyxLayerCrystals(seed: Long, parent: TWorldGenLayer[BNyxColumn])
extends BWorldGenLayerStructuresSparse[TWorldGenColumnFn, BNyxColumn](
	seed, 47199, parent, new NyxLayerCrystals.StructureType(), NyxLayerCrystals.variance, NyxLayerCrystals.margin
) {
	override protected def defaultColumn(xBlock: Int, zBlock: Int) = NyxLayerCrystals.noOp
}
