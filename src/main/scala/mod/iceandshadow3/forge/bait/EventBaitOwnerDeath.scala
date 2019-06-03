package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.basics.util.{LogicPair, LogicTriad}
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.forge.fish.IEventFishOwnerDeath
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingDeathEvent

class EventBaitOwnerDeath extends BEventBaitOwner[LivingDeathEvent] {
	override type FishType = IEventFishOwnerDeath

	override protected def catchFish(logicpair: LogicPair[BLogicItem]) =
		logicpair.logic.getEventFish[IEventFishOwnerDeath](logicpair.variant)

	override protected def handleFish(
		event: LivingDeathEvent,
		item: WRefItem,
		logictriad: LogicTriad[BLogicItem],
		fish: IEventFishOwnerDeath
	): Unit = {
		val cancel = if (event.getSource == DamageSource.OUT_OF_WORLD) {
			fish.onOwnerVoided(logictriad.variant, logictriad.state, item, event.isCanceled)
		} else fish.onOwnerDeath(logictriad.variant, logictriad.state, item, event.isCanceled)
		//TODO: The following has happened twice now. Add foreaches to E3vl.
		cancel.forBoolean(uncancel => {event.setCanceled(!uncancel)})
	}
}