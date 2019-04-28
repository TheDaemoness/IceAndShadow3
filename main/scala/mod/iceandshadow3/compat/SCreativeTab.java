package mod.iceandshadow3.compat;

import mod.iceandshadow3.IceAndShadow3;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

class SCreativeTab {
	private static class Tab extends ItemGroup {
		//@ObjectHolder(ModInfo.MODID+":strange_talisman")
		private static final Item talisman = null;
		
		public Tab() {super(IceAndShadow3.MODID);}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(talisman);
		}
	}
	public static final ItemGroup GROUP = new SCreativeTab.Tab();
}