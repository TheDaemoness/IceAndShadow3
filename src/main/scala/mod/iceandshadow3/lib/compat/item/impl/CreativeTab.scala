package mod.iceandshadow3.lib.compat.item.impl

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.multiverse.DomainNyx
import net.minecraft.item.{ItemGroup, ItemStack}
import net.minecraft.util.NonNullList

protected[compat] object CreativeTab extends ItemGroup(IaS3.MODID) {
	override def fill(list: NonNullList[ItemStack]): Unit = {
		super.fill(list)
		list.sort((a, b) => a.getItem.getRegistryName.getPath.compareTo(b.getItem.getRegistryName.getPath))
	}
	override def hasSearchBar = true
	override def getBackgroundImage = ItemGroup.SEARCH.getBackgroundImage

	override def createIcon: ItemStack =
		new ItemStack(BinderItem(DomainNyx.Items.wayfinder))
}
