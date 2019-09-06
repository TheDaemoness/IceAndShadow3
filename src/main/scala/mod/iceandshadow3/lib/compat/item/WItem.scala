package mod.iceandshadow3.lib.compat.item

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

case class WItem(final override protected[item] val exposeItem: Item) extends BWItem with Comparable[WItem] {
	def this(id: String) = this(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)))
	override def asWItem(): WItem = this

	override def compareTo(t: WItem) = {
		val a = exposeItem
		val b = t.exposeItem
		if(a == null) {if(a == b) 0 else -1}
		else if(b == null) 1
		else a.getRegistryName.compareTo(b.getRegistryName)
	}
}
