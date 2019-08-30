package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.base.BWorldGenColumn
import mod.iceandshadow3.lib.compat.block.`type`.{CommonBlockTypes, TBlockStateSource}

abstract class BColumn extends BWorldGenColumn {
	protected def blockDefault(i: Int): TBlockStateSource =
		if (i <= 8) WorldGenNyx.exousia else CommonBlockTypes.AIR
}
