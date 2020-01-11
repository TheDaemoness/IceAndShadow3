package mod.iceandshadow3.lib.compat.forge.cap

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

object InventoryAccess {
	type SidedFactory = TSideMap.Factory[IInventory, IItemHandler]
	type Factory = IInventory => IItemHandler

	def raw = (inv: IInventory) => new AForgeItemHandler(inv) {
		override def remap(slot: Int) = slot
		override def getSlots = underlying.getSizeInventory
	}
	def none = (inv: IInventory) => new IItemHandler {
		override def getSlots = 0
		override def getStackInSlot(slot: Int) = ItemStack.EMPTY
		override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean) = stack
		override def extractItem(slot: Int, amount: Int, simulate: Boolean) = ItemStack.EMPTY
		override def getSlotLimit(slot: Int) = 0
		override def isItemValid(slot: Int, stack: ItemStack) = false
	}
	def slot(slotId: Int) = (inv: IInventory) => new AForgeItemHandler(inv) {
		override def remap(slot: Int) = slotId
		override def getSlots = 1
	}
	//TODO: More variants!
}
