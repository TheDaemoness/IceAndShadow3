package mod.iceandshadow3.lib.compat.item

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

case class WItem(final override protected val exposeItem: Item) extends BWItem {
	def this(id: String) = this(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)))
}
