package mod.iceandshadow3.lib.compat.forge.bait

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import mod.iceandshadow3.lib.compat.forge.fish.TEventFishOwnerDeath
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent

class EventBaitOwnerDeath extends EventBaitOwner[LivingDeathEvent] {
	override type FishType = TEventFishOwnerDeath

	override protected def catchFish(logic: BLogicItem) = logic.facet[TEventFishOwnerDeath]

	override protected def handleFish(
		event: LivingDeathEvent,
		item: WItemStackOwned[WEntityLiving],
		logic: BLogicItem,
		fish: TEventFishOwnerDeath
	): Unit = {
		val cancel = if (event.getSource == DamageSource.OUT_OF_WORLD) {
			fish.onOwnerVoided(item, event.isCanceled)
		} else fish.onOwnerDeath(item, event.isCanceled)
		cancel.forBoolean(uncancel => {event.setCanceled(!uncancel)})
	}
}