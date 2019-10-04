package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.`type`.TBlockStateSource
import mod.iceandshadow3.lib.gen.{BWorldGenRegion, WorldGenColumn}

abstract class BNyxColumn extends BWorldGenRegion.BColumnTransformer {
	def apply(i: Int): TBlockStateSource
	val bedrock: TBlockStateSource

	override def first(out: WorldGenColumn): Unit = out(0) = bedrock
	override def apply(out: WorldGenColumn, y: Int): Unit = {
		val result = apply(y-1)
		if(result != null) out(y) = result
	}
}
