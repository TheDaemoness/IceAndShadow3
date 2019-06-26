package mod.iceandshadow3.lib.compat.item.impl

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.multiverse.DomainNyx
import net.minecraft.item.{ItemGroup, ItemStack}

protected[compat] object CreativeTab extends ItemGroup(IaS3.MODID) {
	override def createIcon: ItemStack =
		new ItemStack(BinderItem(DomainNyx.Items.wayfinder)(0))
}
