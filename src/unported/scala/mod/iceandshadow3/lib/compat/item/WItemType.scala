package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.{BLogicBlock, BLogicItem}
import mod.iceandshadow3.lib.compat.block.impl.BinderBlock
import mod.iceandshadow3.lib.compat.id.WIdItem
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

case class WItemType(final override val asItem: Item) extends WItem with Comparable[WItemType] {
	def this(id: String) = this(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)))
	override def asWItem(): WItemType = this

	override def compareTo(t: WItemType) = {
		val a = asItem
		val b = t.asItem
		if(a == null) {if(a == b) 0 else -1}
		else if(b == null) 1
		else a.getRegistryName.compareTo(b.getRegistryName)
	}
}
object WItemType {
	@throws[NoSuchElementException]
	def apply(name: String): WItemType = WIdItem(name).getOrThrow
	private[lib] def make(what: BLogicItem): WItemType = new WItemType(BinderItem.apply(what))
	private[lib] def make(what: BLogicBlock): WItemType = new WItemType(BinderBlock.apply(what)._2)
}
