package mod.iceandshadow3.multiverse.dim_nyx.feature

import java.util.Random

import mod.iceandshadow3.lib.gen.{BWorldGenFeatureTypeBuilt, CanvasFeature}
import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn, RandomXZ, TupleXZ}
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.dim_nyx.NyxIsleProperties
import mod.iceandshadow3.multiverse.dim_nyx.column.{BNyxColumn, BNyxColumnIsle}

class NyxFeatureTree(seed: Long, isleinfo: IPosColumn => NyxIsleProperties)
extends BWorldGenFeatureTypeBuilt[BNyxColumn](
	new CanvasFeature(DomainGaia, 9, 256, 9)
) {
	private val center = TupleXZ(4,4)
	private val trunk = DomainGaia.Blocks.petrified_log.asWBlockState(0).asBlockFn
	class Analysis {
		var height: Int = 0
		var rng: Random = _
	}

	/** Called to check if this feature instance differs from the base. */
	override def analyze(existing: IMap2d[BNyxColumn], origin: IPosColumn) = {
		val col = IPosColumn.lookupFrom(existing, origin, center.x, center.z)
		val height = col.height.toInt
		if(
			!MathUtils.isInRadius(96, origin.xBlock, origin.zBlock) &&
			(height > 76 && height < 172) &&
			isleinfo(origin).trees &&
			!col.hasCaveAt(height)
		) {
			val retval = new Analysis
			retval.height = height
			retval.rng = new RandomXZ(seed, 44411, origin.xBlock, origin.zBlock)
			retval
		} else null
	}

	override def build(analysis: Analysis) = {
		val tree = makeBaseCopy
		//TODO: Tree generation logic as a function of CanvasFeature and the relevant cell.
		tree.column(trunk, center, analysis.height to analysis.height + 8 + analysis.rng.nextInt(6))
		//TODO: Leaves.
		tree
	}
}
