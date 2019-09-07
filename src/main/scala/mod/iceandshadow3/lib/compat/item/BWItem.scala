package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.{ILogicItemProvider, LogicPair}
import mod.iceandshadow3.lib.compat.block.WBlockState
import net.minecraft.item.{BlockItem, Item, Items}
import net.minecraft.tags.ItemTags
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

abstract class BWItem extends ILogicItemProvider
{
	protected[item] def exposeItem(): Item
	def asWItem(): WItem
	def asWItemStack(): WItemStack = new WItemStack(exposeItem().getDefaultInstance, null)

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

	override def getLogicPair: LogicPair[BLogicItem] =
		exposeItem() match {
			case lp: ILogicItemProvider => lp.getLogicPair
			case _ => null
		}

	def isEmpty: Boolean = exposeItem() == Items.AIR

	def registryName: String = ForgeRegistries.ITEMS.getKey(exposeItem()).toString
}
