package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.compat.misc.{TWUsageOnBlock, WUsage}
import net.minecraft.item.BlockItemUseContext

class WUsagePlace(override protected val biuc: BlockItemUseContext)
extends WUsage(biuc.getItem, biuc.getPlayer, biuc.getHand, biuc.isPlacerSneaking)
with TWUsageOnBlock
