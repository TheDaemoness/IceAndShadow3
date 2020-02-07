package mod.iceandshadow3.lib.compat.id

import mod.iceandshadow3.lib.compat.block.WBlockType
import mod.iceandshadow3.lib.compat.item.WItem
import net.minecraft.tags.{BlockTags, ItemTags}
import net.minecraft.util.ResourceLocation

import scala.language.implicitConversions

// Important: DO NOT USE tag.contains!

final class WIdTagItem(vanilla: ResourceLocation) extends WId(vanilla) {
	private lazy val tag = ItemTags.getCollection.get(vanilla)
	def unapply(what: WItem) = if(tag == null) false else what.asItem().isIn(tag)
}
object WIdTagItem {
	implicit def apply(fullname: String): WIdTagItem = new WIdTagItem(new ResourceLocation(fullname))
}

final class WIdTagBlock(vanilla: ResourceLocation) extends WId(vanilla) {
	private lazy val tag = BlockTags.getCollection.get(vanilla)
	def unapply(what: WBlockType): Boolean = if(tag == null) false else what.asBlock().isIn(tag)
	def unapply(what: WItem): Boolean = what.toBlockState.fold(false)(unapply)
}
object WIdTagBlock {
	implicit def apply(fullname: String): WIdTagBlock = new WIdTagBlock(new ResourceLocation(fullname))
}
