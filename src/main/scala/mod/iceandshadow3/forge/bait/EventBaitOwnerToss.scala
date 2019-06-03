package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.compat.item.WRefItem
import mod.iceandshadow3.forge.fish.IEventFishOwnerToss
import net.minecraftforge.event.entity.item.ItemTossEvent

//WARNING: Despite the name, this event needs to be handled very differently from the other Owner events.
//DO NOT SUBTYPE BEventBaitOwner HERE!

class EventBaitOwnerToss extends BEventBait[ItemTossEvent] {
	override def receiveCancelled = false

	override protected def handle(event: ItemTossEvent): Unit ={
		val item = new WRefItem(event.getEntityItem.getItem, event.getPlayer)
		forEventFish[IEventFishOwnerToss, BLogicItem, Unit](item, (triad, feesh) => {
			feesh.onOwnerToss(triad.variant, triad.state, item).forBoolean(uncancel => {event.setCanceled(!uncancel)})
		})
	}
}
