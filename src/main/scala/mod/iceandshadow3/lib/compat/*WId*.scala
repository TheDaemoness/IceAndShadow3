package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.block.BWBlockType
import mod.iceandshadow3.lib.compat.item.WItemType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistryEntry}

class WId(protected[compat] val asVanilla: ResourceLocation) extends TNamed[WId] {
	def this(what: IForgeRegistryEntry[_]) = this(what.getRegistryName)
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	final def id = this
	final override def namespace: String = asVanilla.getNamespace
	final override def name: String = asVanilla.getPath
	final override def toString = asVanilla.toString
}

abstract class BWId[Return](vanilla: ResourceLocation) extends WId(vanilla) {
	def unapply: Option[Return]
}

final class WIdItem(vanilla: ResourceLocation) extends BWId[WItemType](vanilla) {
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	override def unapply = {
		val item = ForgeRegistries.ITEMS.getValue(asVanilla)
		if(item == null) None else Some(new WItemType(item))
	}
}
object WIdItem {
	implicit def apply(fullname: String): WIdItem = new WIdItem(new ResourceLocation(fullname))
}

final class WIdBlock(vanilla: ResourceLocation) extends BWId[BWBlockType](vanilla) {
	def this(namespace: String, fullname: String) = this(new ResourceLocation(namespace, fullname))
	override def unapply = {
		val block = ForgeRegistries.BLOCKS.getValue(asVanilla)
		if(block == null) None else Some(new BWBlockType {
			override protected[compat] def asBlock() = block
		})
	}
}
