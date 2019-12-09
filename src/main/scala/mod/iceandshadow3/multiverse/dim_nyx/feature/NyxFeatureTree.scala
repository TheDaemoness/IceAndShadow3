package mod.iceandshadow3.multiverse.dim_nyx.feature

import java.util.Random

import mod.iceandshadow3.lib.compat.block.BlockQueries
import mod.iceandshadow3.lib.gen.{BWorldGenFeatureTypeBuilt, CanvasFeature}
import mod.iceandshadow3.lib.spatial.{IMap2d, IPosColumn, RandomXZ, TupleXZ}
import mod.iceandshadow3.lib.util.MathUtils
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.dim_nyx.column.BNyxColumn

object NyxFeatureTree {
	//Annoying hard-coded array of 9-long arrays of integers that get mapped to leaf block states.
	val largeleafopts = Array(
		Array(0,3,2,3,2,1,2,1,0),
		Array(3,2,3,2,1,2,1,0,1),
		Array(2,3,0,1,2,3,0,1,2),
		Array(3,2,1,2,1,0,3,2,1),
		Array(2,1,2,1,0,1,2,1,2),
		Array(1,2,3,0,1,2,1,2,3),
		Array(2,1,0,3,2,1,0,3,2),
		Array(1,0,1,2,1,2,3,2,3),
		Array(0,1,2,1,2,3,2,3,0)
	)
	private val center = TupleXZ(2,2)
	val trunk = DomainGaia.Blocks.petrified_log.asWBlockState(0).asBlockFn
	private val leaves = DomainGaia.Blocks.petrified_leaves.asWBlockState(0)
	val leaves_1 = leaves.+(DomainGaia.Blocks.petrified_leaves.varSupport, 2).asBlockFn
	val leaves_2 = leaves.+(DomainGaia.Blocks.petrified_leaves.varSupport, 1).asBlockFn
	val leaves_3 = leaves.+(DomainGaia.Blocks.petrified_leaves.varSupport, 0).asBlockFn
}
class NyxFeatureTree(seed: Long, shouldTree: IPosColumn => Boolean)
extends BWorldGenFeatureTypeBuilt[BNyxColumn](
	new CanvasFeature(DomainGaia, 5, 256, 5, (col,b,y) => {
		val existing = col.apply(y)
		if(BlockQueries.notTougher(existing)(b)) col.update(y, b)
	})
) {
	class Analysis {
		var height: Int = 0
		var rng: Random = _
	}
	import NyxFeatureTree.center

	/** Called to check if this feature instance differs from the base. */
	override def analyze(existing: IMap2d[BNyxColumn], origin: IPosColumn) = {
		val col = IPosColumn.lookupFrom(existing, origin, center.x, center.z)
		val height = col.height.toInt
		if(
			!MathUtils.isInRadius(96, origin.xBlock, origin.zBlock) &&
			(height > 76 && height < 172) &&
			!col.hasCaveAt(height) &&
			shouldTree(origin)
		) {
			val retval = new Analysis
			retval.height = height
			retval.rng = new RandomXZ(seed, 44411, origin.xBlock, origin.zBlock)
			retval
		} else null
	}

	protected def leavesTop(tree: CanvasFeature, max: Int) = {
		import NyxFeatureTree.leaves_1
		tree.one(leaves_1, center, max+1)
		tree.one(leaves_1, center.x+1, max, center.z)
		tree.one(leaves_1, center.x, max, center.z+1)
		tree.one(leaves_1, center.x-1, max, center.z)
		tree.one(leaves_1, center.x, max, center.z-1)
	}

	protected def leavesLarge(tree: CanvasFeature, level: Int, rng: Random): Unit = {
		val xOpt = rng.nextInt(3)
		val zOpt = rng.nextInt(3)
		val leaves = NyxFeatureTree.largeleafopts(xOpt+zOpt*3)
		val xAt = center.x-2+xOpt
		val zAt = center.z-2+zOpt
		var xi = 0
		while(xi < 3) {
			var zi = 0
			while(zi < 3) {
				leaves(xi+zi*3) match {
					case 0 =>
					case 1 => tree.one(NyxFeatureTree.leaves_1, xAt+xi, level, zAt+zi)
					case 2 => tree.one(NyxFeatureTree.leaves_2, xAt+xi, level, zAt+zi)
					case 3 => tree.one(NyxFeatureTree.leaves_3, xAt+xi, level, zAt+zi)
				}
				zi += 1
			}
			xi += 1
		}
	}

	protected def leavesSmall(tree: CanvasFeature, level: Int, rng: Random): Unit = {
		val xz = rng.nextInt(4) match {
			case 0 => (1,-1)
			case 1 => (1,1)
			case 2 => (-1,1)
			case 3 => (-1,-1)
		}
		import NyxFeatureTree.leaves_2
		import NyxFeatureTree.leaves_1
		tree.one(leaves_2, center.x+xz._1, level, center.z+xz._2)
		tree.one(leaves_1, center.x+xz._1, level, center.z)
		tree.one(leaves_1, center.x, level, center.z+xz._2)
	}

	override def build(analysis: Analysis) = {
		val tree = makeBaseCopy
		val max = analysis.height + 8 + analysis.rng.nextInt(6)
		//TODO: Tree generation logic as a function of CanvasFeature and the relevant cell.
		leavesTop(tree, max)
		import NyxFeatureTree.trunk
		tree.column(trunk, center, analysis.height to max)
		leavesLarge(tree, max-1, analysis.rng)
		leavesLarge(tree, max-2, analysis.rng)
		leavesSmall(tree, max-(2+analysis.rng.nextInt(4)), analysis.rng)
		tree
	}
}
