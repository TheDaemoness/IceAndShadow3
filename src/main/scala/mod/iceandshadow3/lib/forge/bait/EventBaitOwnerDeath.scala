package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.util.{LogicPair, LogicTriad}
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.forge.fish.TEventFishOwnerDeath
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent

class EventBaitOwnerDeath extends BEventBaitOwner[LivingDeathEvent] {
	override type FishType = TEventFishOwnerDeath

	override protected def catchFish(logicpair: LogicPair[BLogicItem]) =
		logicpair.logic.getEventFish[TEventFishOwnerDeath](logicpair.variant)

	override protected def handleFish(
		event: LivingDeathEvent,
		item: WItemStack,
		logictriad: LogicTriad[BLogicItem],
		fish: TEventFishOwnerDeath
	): Unit = {
		val cancel = if (event.getSource == DamageSource.OUT_OF_WORLD) {
			fish.onOwnerVoided(logictriad.variant, logictriad.state, item, event.isCanceled)
		} else fish.onOwnerDeath(logictriad.variant, logictriad.state, item, event.isCanceled)
		cancel.forBoolean(uncancel => {event.setCanceled(!uncancel)})
	}
}