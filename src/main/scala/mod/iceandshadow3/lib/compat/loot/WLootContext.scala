package mod.iceandshadow3.lib.compat.loot

import mod.iceandshadow3.lib.compat.world.TWWorld
import net.minecraft.world.storage.loot.LootContext

class WLootContext(private[loot] val expose: LootContext) extends TWWorld {
	override protected[compat] def exposeWorld() = expose.getWorld
	def luck = expose.getLuck
	override def rng() = expose.getRandom
}
