package mod.iceandshadow3.multiverse.dim_nyx.feature

import mod.iceandshadow3.lib.gen.{BWorldGenLayerFeaturesSparse, TWorldGenColumnFn, TWorldGenLayer}
import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.dim_nyx.NyxIsleProperties
import mod.iceandshadow3.multiverse.dim_nyx.column.BNyxColumn

// TODO: FeaturesDense, once written, is better to use here.

class NyxLayerTrees(seed: Long, parent: TWorldGenLayer[BNyxColumn], isleinfo: IPosColumn => NyxIsleProperties)
extends BWorldGenLayerFeaturesSparse[TWorldGenColumnFn, BNyxColumn](
	seed, 404714, parent, new NyxFeatureTree(seed, isleinfo), 9, 0
) {
	override protected def defaultColumn(xBlock: Int, zBlock: Int) = TWorldGenColumnFn.noOp(DomainGaia)
}
