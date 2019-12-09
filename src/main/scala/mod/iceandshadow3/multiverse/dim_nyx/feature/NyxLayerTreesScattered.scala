package mod.iceandshadow3.multiverse.dim_nyx.feature

import mod.iceandshadow3.lib.gen.{BWorldGenLayerFeaturesSparse, TWorldGenColumnFn, TWorldGenLayer}
import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.dim_nyx.NyxIsleProperties
import mod.iceandshadow3.multiverse.dim_nyx.column.BNyxColumn

// TODO: FeaturesDense, once written, is better to use here.

class NyxLayerTreesScattered(seed: Long, parent: TWorldGenLayer[BNyxColumn], isleinfo: IPosColumn => NyxIsleProperties)
extends BWorldGenLayerFeaturesSparse[TWorldGenColumnFn, BNyxColumn](
	seed, 17004, parent, new NyxFeatureTree(seed, isleinfo(_).treesSparse), 12, 0
) {
	override protected def defaultColumn(xBlock: Int, zBlock: Int) = TWorldGenColumnFn.noOp(DomainGaia)
}
