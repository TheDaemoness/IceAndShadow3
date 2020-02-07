package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.base.{ProviderLogic, TNamed}
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.id.WIdItem
import mod.iceandshadow3.lib.util.GeneralUtils
import net.minecraft.item.{BlockItem, Item, Items}
import net.minecraft.tags.ItemTags
import net.minecraft.util.{IItemProvider, ResourceLocation}
import net.minecraftforge.registries.ForgeRegistries

import scala.reflect.ClassTag

abstract class WItem extends ProviderLogic.Item with IItemProvider with TNamed[WIdItem] {
	override def asItem(): Item
	def asWItem(): WItemType
	def asWItemStack(): WItemStack = new WItemStack(asItem().getDefaultInstance)
	def asWItemStack[Owner <: WEntity](owner: Owner): WItemStackOwned[Owner] =
		new WItemStackOwned(asItem().getDefaultInstance, owner)
	def getBlock: Option[WBlockState] = asItem() match {
		case bi: BlockItem => Some(new WBlockState(bi.getBlock.getDefaultState))
		case _ => None
	}

	def toBlockState: Option[WBlockState] = asItem() match {
		case bli: BlockItem => Some(new WBlockState(bli.getBlock.getDefaultState))
		case _ => None
	}

	override def getLogic: BLogicItem =
		asItem() match {
			case lp: ProviderLogic.Item => lp.getLogic
			case _ => null
		}

	def isEmpty: Boolean = asItem() == Items.AIR

	def registryName: String = id.toString
	override def name: String = ForgeRegistries.ITEMS.getKey(asItem()).getPath
	override def namespace: String = ForgeRegistries.ITEMS.getKey(asItem()).getNamespace

	override def facet[What <: Object : ClassTag] = asItem() match {
		case lp: ProviderLogic.Item => lp.facet[What]
		case item => GeneralUtils.cast[What](item)
	}
	def matches(what: WItem) =
		asItem() == what.asItem()
	def matches(what: BLogicItem) = {
		val lp = getLogic
		lp != null && lp == what
	}

	override def id: WIdItem = new WIdItem(asItem().getRegistryName)
}
