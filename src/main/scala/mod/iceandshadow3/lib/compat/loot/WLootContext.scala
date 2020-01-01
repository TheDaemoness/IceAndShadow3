package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.compat.world.TWWorld
import net.minecraft.world.storage.loot.LootContext

class WLootContext(private[loot] val unapply: LootContext) extends TWWorld {
	override protected[compat] def exposeWorld() = unapply.getWorld
	def luck = unapply.getLuck
	override def rng() = unapply.getRandom
}
