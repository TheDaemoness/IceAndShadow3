package mod.iceandshadow3.compat;

import mod.iceandshadow3.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

class SCreativeTab {
	private static class Tab extends CreativeTabs {
		@ObjectHolder(ModInfo.MODID+":strange_talisman")
		private static final Item talisman = null;
		
		public Tab() {super(ModInfo.MODID);}

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(talisman);
		}
	}
	private static final CreativeTabs tab = new SCreativeTab.Tab();
	static final void add(Item it) {
		it.setCreativeTab(tab);
	}
	static final void add(Block bl) {
		bl.setCreativeTab(tab);
	}
}