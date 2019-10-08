package mod.iceandshadow3.lib.compat.item

import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

case class WItemType(final override protected[item] val exposeItem: Item) extends BWItem with Comparable[WItemType] {
	def this(id: String) = this(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)))
	override def asWItem(): WItemType = this

	override def compareTo(t: WItemType) = {
		val a = exposeItem
		val b = t.exposeItem
		if(a == null) {if(a == b) 0 else -1}
		else if(b == null) 1
		else a.getRegistryName.compareTo(b.getRegistryName)
	}
}