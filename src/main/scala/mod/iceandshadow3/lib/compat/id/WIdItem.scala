package mod.iceandshadow3.lib.compat.id

import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.item.WItemType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

import scala.language.implicitConversions

final class WIdItem(vanilla: ResourceLocation) extends WRegistryId[WItemType](vanilla) with TNamed[WIdItem] {
	override protected def translationKeyPrefix = "item"
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	override def id = this
	override def get = {
		val item = ForgeRegistries.ITEMS.getValue(asVanilla)
		if(item == null) None else Some(new WItemType(item))
	}
}
object WIdItem {
	implicit def apply(fullname: String): WIdItem = new WIdItem(new ResourceLocation(fullname))
}
