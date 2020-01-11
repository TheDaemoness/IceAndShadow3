package mod.iceandshadow3.multiverse.dim_nyx.feature

import mod.iceandshadow3.lib.gen.{WorldGenLayerFeaturesSparse, TWorldGenColumnFn, TWorldGenLayer}
import mod.iceandshadow3.lib.spatial.IPosColumn
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.dim_nyx.NyxIsleProperties
import mod.iceandshadow3.multiverse.dim_nyx.column.ColumnFnNyx

// TODO: FeaturesDense, once written, is better to use here.

class NyxLayerTreesForest(seed: Long, parent: TWorldGenLayer[ColumnFnNyx], isleinfo: IPosColumn => NyxIsleProperties)
extends WorldGenLayerFeaturesSparse[TWorldGenColumnFn, ColumnFnNyx](
	seed, 404714, parent, new NyxFeatureTree(seed, isleinfo(_).treesDense), 6, 0
) {
	override protected def defaultColumn(xBlock: Int, zBlock: Int) = TWorldGenColumnFn.noOp(DomainGaia)
}
