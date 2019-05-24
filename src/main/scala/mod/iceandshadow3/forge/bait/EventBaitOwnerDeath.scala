package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.basics.util.{LogicPair, LogicTriad}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.forge.fish.IEventFishOwnerDeath
import mod.iceandshadow3.util.L3
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.IEventBus

class EventBaitOwnerDeath(bus: IEventBus) extends BEventBaitOwner[LivingDeathEvent](bus) {
	override type FishType = IEventFishOwnerDeath

	override protected def catchFish(logicpair: LogicPair[BLogicItem]) =
		logicpair.logic.getEventFish[IEventFishOwnerDeath](logicpair.variant)

	override protected def handleFish(
		event: LivingDeathEvent,
		item: CRefItem,
		logictriad: LogicTriad[BLogicItem],
		fish: IEventFishOwnerDeath
	): Unit = {
		val cancel = if (event.getSource == DamageSource.OUT_OF_WORLD) {
			fish.onOwnerVoided(logictriad.variant, logictriad.state, item, event.isCanceled)
		} else fish.onOwnerDeath(logictriad.variant, logictriad.state, item, event.isCanceled)
		//TODO: The following has happened twice now. Add foreaches to L3.
		if (cancel != L3.NULL) event.setCanceled(cancel == L3.FALSE)
	}
}