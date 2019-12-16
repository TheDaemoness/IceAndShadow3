package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.LogicPair
import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import mod.iceandshadow3.lib.forge.fish.TEventFishOwnerDeath
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent

class EventBaitOwnerDeath extends BEventBaitOwner[LivingDeathEvent] {
	override type FishType = TEventFishOwnerDeath

	override protected def catchFish(logicpair: LogicPair[BLogicItem]) = logicpair.logic.facet[TEventFishOwnerDeath]

	override protected def handleFish(
		event: LivingDeathEvent,
		item: WItemStackOwned[WEntityLiving],
		lp: LogicPair[BLogicItem],
		fish: TEventFishOwnerDeath
	): Unit = {
		val cancel = if (event.getSource == DamageSource.OUT_OF_WORLD) {
			fish.onOwnerVoided(lp.variant, item, event.isCanceled)
		} else fish.onOwnerDeath(lp.variant, item, event.isCanceled)
		cancel.forBoolean(uncancel => {event.setCanceled(!uncancel)})
	}
}