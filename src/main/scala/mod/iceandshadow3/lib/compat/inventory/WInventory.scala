package mod.iceandshadow3.lib.compat.inventory

import mod.iceandshadow3.lib.compat.block.impl.ATileEntity
import mod.iceandshadow3.lib.compat.item.{ItemUtils, WItemStack}
import mod.iceandshadow3.lib.item.ItemSeq
import mod.iceandshadow3.lib.util.E3vl
import net.minecraft.inventory.IInventory

class WInventory(private[compat] val expose: IInventory) extends ItemSeq {
	def countFilled = expose match {
		case ate: ATileEntity => ate.countFilled //Is there actually a point of having this match check?
		case _ => count(!_.isEmpty)
	}

	def add(what: WItemStack): Boolean = {
		if (what.isEmpty) return true
		val whatexposed = what.expose()
		for (i <- 0 until expose.getSizeInventory) {
			val current = expose.getStackInSlot(i)
			if (current == null || current.isEmpty) { //Empty slot.
				if (expose.isItemValidForSlot(i, whatexposed)) {
					expose.setInventorySlotContents(i, what.copyAndDestroy())
					return true
				}
			} else { //Not an empty slot, try to stack.
				if (ItemUtils.stackInto(current, what.expose()) != 0) {
					expose.markDirty()
					if (what.isEmpty) return true
				}
			}
		}
		false
	}

	/** Adds this item if a stack of the same type does not already exist.
		* @return True if the item was added, neutral if it existed, and false if it couldn't be added.
		*/
	def donate(item: WItemStack): E3vl = {
		var slot = -1
		for(i <- 0 until expose.getSizeInventory) {
			val stack = expose.getStackInSlot(i)
			if(slot == -1 && (stack == null || stack.isEmpty) && expose.isItemValidForSlot(i, item.expose())) slot = i
			if(item.matches(stack)) return E3vl.NEUTRAL
		}
		if(slot == -1) E3vl.FALSE
		else {expose.setInventorySlotContents(slot, item.copyAndDestroy()); E3vl.TRUE}
	}

	override def update(idx: Int, elem: WItemStack): Unit = {
		val stack = elem.expose()
		if(expose.isItemValidForSlot(idx, stack)) expose.setInventorySlotContents(idx, stack)
		else expose.removeStackFromSlot(idx)
	}
	override def length = expose.getSizeInventory
	override def apply(idx: Int) = new WItemStack(expose.getStackInSlot(idx))
	override def iterator: Iterator[WItemStack] = new Iterator[WItemStack] {
		var index = 0
		override def hasNext = index < expose.getSizeInventory

		override def next() = {
			index += 1
			new WItemStack(expose.getStackInSlot(index-1))
		}
	}
}
