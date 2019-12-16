package mod.iceandshadow3.lib.forge.bait

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.item.WItemStackOwned
import mod.iceandshadow3.lib.forge.fish.TEventFishOwnerToss
import net.minecraftforge.event.entity.item.ItemTossEvent

//WARNING: Despite the name, this event needs to be handled very differently from the other Owner events.
//DO NOT SUBTYPE BEventBaitOwner HERE!

class EventBaitOwnerToss extends BEventBait[ItemTossEvent] {
	override def receiveCancelled = false

	override protected def handle(event: ItemTossEvent): Unit = {
		val item = new WItemStackOwned(event.getEntityItem.getItem, CNVEntity.wrap(event.getPlayer))
		forEventFish[TEventFishOwnerToss, BLogicItem, Unit](item, (pair, feesh) => {
			feesh.onOwnerToss(pair.variant,  item).forBoolean(uncancel => {event.setCanceled(!uncancel)})
		})
	}
}
