package mod.iceandshadow3.multiverse.dim_nyx.feature

import mod.iceandshadow3.lib.gen.{WorldGenLayerFeaturesSparse, TWorldGenColumnFn, TWorldGenLayer}
import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.dim_nyx.NyxIsleProperties
import mod.iceandshadow3.multiverse.dim_nyx.column.ColumnFnNyx

// TODO: FeaturesDense, once written, is better to use here.

class NyxLayerTreesScattered(seed: Long, parent: TWorldGenLayer[ColumnFnNyx], isleinfo: IPosColumn => NyxIsleProperties)
extends WorldGenLayerFeaturesSparse[TWorldGenColumnFn, ColumnFnNyx](
	seed, 17004, parent, new NyxFeatureTree(seed, isleinfo(_).treesSparse), 12, 0
) {
	override protected def defaultColumn(xBlock: Int, zBlock: Int) = TWorldGenColumnFn.noOp(DomainGaia)
}
