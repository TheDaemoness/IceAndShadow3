package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.compat.block.WBlockState
import net.minecraft.item.{BlockItem, Item}
import net.minecraft.tags.ItemTags
import net.minecraft.util.ResourceLocation

abstract class BWItem {
	protected def exposeItem(): Item

	def hasTag(tagname: String): Boolean = {
		//TODO: WTag?
		val tag = ItemTags.getCollection.get(new ResourceLocation(tagname))
		if(tag == null) false
		else exposeItem().isIn(tag)
	}

	def toBlockState: Option[WBlockState] = exposeItem() match {
		case bli: BlockItem => Some(new WBlockState(bli.getBlock.getDefaultState))
		case _ => None
	}
}
