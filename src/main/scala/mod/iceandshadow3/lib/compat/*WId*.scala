package mod.iceandshadow3.lib.compat

import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.block.BWBlockType
import mod.iceandshadow3.lib.compat.item.{BWItem, WItemType}
import mod.iceandshadow3.lib.util.BFunctionOptions
import net.minecraft.tags.{BlockTags, ItemTags}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistryEntry}

import scala.language.implicitConversions

class WId(protected[compat] val asVanilla: ResourceLocation) extends TNamed[WId] with Comparable[WId] {
	def this(what: IForgeRegistryEntry[_]) = this(what.getRegistryName)
	def this(namespace: String, name: String) = this(new ResourceLocation(namespace, name))
	def id = this
	final override def namespace: String = asVanilla.getNamespace
	final override def name: String = asVanilla.getPath
	final def nameFull: String = asVanilla.toString
	override def toString = nameFull

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
		override protected def discard(what: Int) = what == 0
		override protected def transform(what: Int) = what
		override protected def default = 0
	}
}

abstract class BWId[+Return](vanilla: ResourceLocation) extends WId(vanilla) with Iterable[Return] {
	def get: Option[Return]
	@throws[NoSuchElementException]
	def getOrThrow: Return = get.getOrElse(throw new NoSuchElementException(s"Cannot dereference $this"))
	override def iterator = get.iterator
	override def foreach[U](f: Return => U): Unit = get.foreach(f)
	override def map[B](f: Return => B) = get.map(f)
	final override def toString = nameFull
	def translationKeyPrefix: String
	def translationKey: String = s"$translationKeyPrefix.$namespace.$name"
}

final class WIdItem(vanilla: ResourceLocation) extends BWId[WItemType](vanilla) with TNamed[WIdItem] {
	override def translationKeyPrefix = "item"
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

final class WIdBlock(vanilla: ResourceLocation) extends BWId[BWBlockType](vanilla) with TNamed[WIdBlock] {
	override def translationKeyPrefix = "block"
	def this(namespace: String, fullname: String) = this(new ResourceLocation(namespace, fullname))
	override def id = this
	override def get = {
		val block = ForgeRegistries.BLOCKS.getValue(asVanilla)
		if(block == null) None else Some[BWBlockType](() => block)
	}
}

// Important: DO NOT USE tag.contains!

final class WIdTagItem(vanilla: ResourceLocation) extends WId(vanilla) {
	private lazy val tag = ItemTags.getCollection.get(vanilla)
	def unapply(what: BWItem) = if(tag == null) false else what.asItem().isIn(tag)
}
object WIdTagItem {
	implicit def apply(fullname: String): WIdTagItem = new WIdTagItem(new ResourceLocation(fullname))
}

final class WIdTagBlock(vanilla: ResourceLocation) extends WId(vanilla) {
	private lazy val tag = BlockTags.getCollection.get(vanilla)
	def unapply(what: BWBlockType): Boolean = if(tag == null) false else what.asBlock().isIn(tag)
	def unapply(what: BWItem): Boolean = what.toBlockState.fold(false)(unapply)
}
object WIdTagBlock {
	implicit def apply(fullname: String): WIdTagBlock = new WIdTagBlock(new ResourceLocation(fullname))
}
