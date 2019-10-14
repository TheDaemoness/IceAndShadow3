package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.item.ItemSeq
import net.minecraft.inventory.container.Container

class WContainer(inv: Container) extends ItemSeq {
	private val slots = inv.inventorySlots
	override def iterator: Iterator[WItemStack] = new Iterator[WItemStack] {
		private val slotit = slots.iterator()
		override def hasNext = slotit.hasNext
		override def next() = new WItemStack(slotit.next.getStack, null)
	}

	override def update(idx: Int, elem: WItemStack): Unit = {
		val slot = slots.get(idx)
		val stack = elem.asItemStack()
		if(slot.isItemValid(stack)) slot.putStack(stack)
		else slot.decrStackSize(slot.getSlotStackLimit)
	}
	override def length = slots.size()
	override def apply(idx: Int) = new WItemStack(slots.get(idx).getStack, null)
}
