package mod.iceandshadow3.compat.item

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.world.DomainNyx
import net.minecraft.item.{ItemGroup, ItemStack}

protected[compat] object SCreativeTab extends ItemGroup(IaS3.MODID) {
	override def createIcon: ItemStack =
		new ItemStack(BinderItem(DomainNyx.li_wayfinder)(0))
}
