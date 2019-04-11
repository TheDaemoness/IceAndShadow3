package mod.iceandshadow3.compat;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

//TODO: Manually generated class stub.
public class CRefItem {
	final ItemStack is;
	CRefItem(ItemStack itemstack) {
		is = itemstack;
	}
	public int count() {return is.getCount();}
	public int countMax() {return is.getMaxStackSize();}
}
