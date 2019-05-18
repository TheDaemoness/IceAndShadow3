package mod.iceandshadow3.compat

import mod.iceandshadow3.IceAndShadow3
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.IRegistry

object SCreativeTab extends ItemGroup(IceAndShadow3.MODID) {
	override def createIcon(): ItemStack =
		new ItemStack(mod.iceandshadow3.world.nyx.DomainNyx.wayfinder.getSecrets[Item].get(0))
}