package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.compat.misc.{TWUsageOnBlock, WUsage}
import net.minecraft.item.BlockItemUseContext

class WUsagePlace(final override protected[compat] val expose: BlockItemUseContext)
extends WUsage(expose.getItem, expose.getPlayer, expose.getHand, expose.isPlacerSneaking)
with TWUsageOnBlock
