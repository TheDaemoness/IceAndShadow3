package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.{LogicPair, LogicProvider}
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.util.Casting
import net.minecraft.item.{BlockItem, Item, Items}
import net.minecraft.tags.ItemTags
import net.minecraft.util.{IItemProvider, ResourceLocation}
import net.minecraftforge.registries.ForgeRegistries

import scala.reflect.ClassTag

abstract class BWItem extends LogicProvider.Item with IItemProvider {
	override def asItem(): Item
	def asWItem(): WItemType
	def asWItemStack(): WItemStack = new WItemStack(asItem().getDefaultInstance, null)
	def getBlock: Option[WBlockState] = asItem() match {
		case bi: BlockItem => Some(new WBlockState(bi.getBlock.getDefaultState))
		case _ => None
	}

	def hasTag(tagname: String): Boolean = {
		//TODO: WTag?
		val tag = ItemTags.getCollection.get(new ResourceLocation(tagname))
		if(tag == null) false
		else asItem().isIn(tag)
	}

	def toBlockState: Option[WBlockState] = asItem() match {
		case bli: BlockItem => Some(new WBlockState(bli.getBlock.getDefaultState))
		case _ => None
	}

	override def getLogicPair: LogicPair[BLogicItem] =
		asItem() match {
			case lp: LogicProvider.Item => lp.getLogicPair
			case _ => null
		}

	def isEmpty: Boolean = asItem() == Items.AIR

	def registryName: String = ForgeRegistries.ITEMS.getKey(asItem()).toString
	override def modName: String = ForgeRegistries.ITEMS.getKey(asItem()).getPath
	override def namespace: String = ForgeRegistries.ITEMS.getKey(asItem()).getNamespace

	override def facet[What <: Object : ClassTag] = asItem() match {
		case lp: LogicProvider.Item => lp.facet[What]
		case item => Casting.cast[What](item)
	}
	def matches(what: BWItem) =
		asItem() == what.asItem()
	def matches(what: BLogicItem) = {
		val lp = getLogicPair
		lp != null && lp.logic == what
	}
}
