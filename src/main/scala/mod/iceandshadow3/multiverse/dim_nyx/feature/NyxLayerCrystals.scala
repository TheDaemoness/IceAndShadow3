package mod.iceandshadow3.multiverse.dim_nyx.feature

import mod.iceandshadow3.lib.compat.block.{BlockQueries, WBlockState}
import mod.iceandshadow3.lib.gen.{BWorldGenFeatureTypeSimple, BWorldGenLayerFeaturesSparse, TWorldGenColumnFn, TWorldGenLayer, WorldGenColumn}
import mod.iceandshadow3.multiverse.{DomainGaia, DomainNyx}
import mod.iceandshadow3.multiverse.dim_nyx.WorldGenNyx
import mod.iceandshadow3.multiverse.dim_nyx.column.BNyxColumn

object NyxLayerCrystals {
	val moonstone = DomainGaia.Blocks.moonstone_block.asWBlockState(0)
	val nifelhium = DomainNyx.Blocks.nifelhiumCrystal.asWBlockState(0)
	val variance = 16
	val margin = 6
	def block(height: Int): WBlockState = if(height > WorldGenNyx.yBald) nifelhium else moonstone

	val noOp = TWorldGenColumnFn.noOp(DomainGaia)
	class Column(height: Int) extends TWorldGenColumnFn {
		val block = NyxLayerCrystals.block(height)
		override def domain = block.asWBlockState.getDomain
		override def apply(col: WorldGenColumn): Unit = {
			val (_, realheight) = col.highest(BlockQueries.notReplaceable, height)
			if(realheight > WorldGenNyx.yExousia+16) {
				col.update(realheight, col.apply(realheight-1))
				col.update(realheight+1, block.asWBlockState)
			}
		}
	}
	class FeatureType extends BWorldGenFeatureTypeSimple[TWorldGenColumnFn, BNyxColumn](1, 1) {
		override def columnAt(xRela: Int, zRela: Int, parent: BNyxColumn) = new Column(Math.min(254, parent.height.toInt))
	}
}
class NyxLayerCrystals(seed: Long, parent: TWorldGenLayer[BNyxColumn])
extends BWorldGenLayerFeaturesSparse[TWorldGenColumnFn, BNyxColumn](
	seed, 47199, parent, new NyxLayerCrystals.FeatureType(), NyxLayerCrystals.variance, NyxLayerCrystals.margin
) {
	override protected def defaultColumn(xBlock: Int, zBlock: Int) = NyxLayerCrystals.noOp
}
