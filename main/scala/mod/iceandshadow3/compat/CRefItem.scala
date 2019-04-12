package mod.iceandshadow3.compat

import net.minecraft.item.ItemStack

//TODO: Manually generated class stub.
class CRefItem(private[compat] val is: ItemStack) {
	def count(): Int = is.getCount();
	def countMax(): Int = is.getMaxStackSize();
}
