package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.block.BWBlockType
import mod.iceandshadow3.lib.compat.item.WItemType
import mod.iceandshadow3.lib.util.BFunctionOptions
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistryEntry}

class WId(protected[compat] val asVanilla: ResourceLocation) extends TNamed[WId] with Comparable[WId] {
	def this(what: IForgeRegistryEntry[_]) = this(what.getRegistryName)
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	def id = this
	final override def namespace: String = asVanilla.getNamespace
	final override def name: String = asVanilla.getPath
	final override def toString = asVanilla.toString

	override def compareTo(t: WId) = {
		if(t == null) 1
		else WId.compare(this, t)
	}
}
object WId {
	private val compare = new BFunctionOptions[(WId, WId), Int, Int](
		pair => pair._1.namespace.compareTo(pair._2.namespace),
		pair => pair._1.name.compareTo(pair._2.name)
	) {
		override protected def discard(what: Int) = (what == 0)
		override protected def transform(what: Int) = what
		override protected def default = 0
	}
}

abstract class BWId[Return](vanilla: ResourceLocation) extends WId(vanilla) {
	def unapply: Option[Return]
}

final class WIdItem(vanilla: ResourceLocation) extends BWId[WItemType](vanilla) with TNamed[WIdItem] {
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	override def id = this
	override def unapply = {
		val item = ForgeRegistries.ITEMS.getValue(asVanilla)
		if(item == null) None else Some(new WItemType(item))
	}
}
object WIdItem {
	implicit def apply(fullname: String): WIdItem = new WIdItem(new ResourceLocation(fullname))
}

final class WIdBlock(vanilla: ResourceLocation) extends BWId[BWBlockType](vanilla) with TNamed[WIdBlock] {
	def this(namespace: String, fullname: String) = this(new ResourceLocation(namespace, fullname))
	override def id = this
	override def unapply = {
		val block = ForgeRegistries.BLOCKS.getValue(asVanilla)
		if(block == null) None else Some(new BWBlockType {
			override protected[compat] def asBlock() = block
		})
	}
}
