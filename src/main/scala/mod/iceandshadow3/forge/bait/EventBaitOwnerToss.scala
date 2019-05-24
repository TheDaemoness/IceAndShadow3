package mod.iceandshadow3.forge.bait

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.forge.fish.IEventFishOwnerToss
import mod.iceandshadow3.util.L3
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.eventbus.api.IEventBus

class EventBaitOwnerToss(bus: IEventBus) extends BEventBait[ItemTossEvent](bus) {
	override def receiveCancelled = false
	//WARNING: Despite the name, this event needs to be handled very differently from the other Owner events.
	//DO NOT SUBTYPE BEventBaitOwner HERE!

	override protected def handle(event: ItemTossEvent): Unit ={
		val item = new CRefItem(event.getEntityItem.getItem, event.getPlayer)
		forEventFish[IEventFishOwnerToss, BLogicItem, Unit](item, (triad, feesh) => {
			val result = feesh.onOwnerToss(triad.variant, triad.state, item)
			if(result != L3.NULL) event.setCanceled(result == L3.FALSE)
		})
	}
}
