package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.block.`type`.{CommonBlockTypes, TBlockStateSource}
import mod.iceandshadow3.lib.gen.BGeneratedColumn

abstract class BNyxColumn extends BGeneratedColumn {
	protected def blockDefault(i: Int): TBlockStateSource =
		if (i <= 8) WorldGenNyx.exousia else CommonBlockTypes.AIR
}
