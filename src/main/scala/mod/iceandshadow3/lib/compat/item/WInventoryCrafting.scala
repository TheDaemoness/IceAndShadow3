package mod.iceandshadow3.lib.compat.item

import net.minecraft.inventory.CraftingInventory

class WInventoryCrafting(inv: CraftingInventory) extends WInventory(inv) {
	def width = inv.getWidth
	def height = inv.getHeight
}
