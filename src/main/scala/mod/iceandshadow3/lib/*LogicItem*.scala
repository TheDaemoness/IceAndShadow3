package mod.iceandshadow3.lib

import java.util.function.{Consumer, Predicate}

import javax.annotation.Nullable
import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.base.BLogicWithItem
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.lib.compat.item._
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import mod.iceandshadow3.lib.forge.fish.TEventFishOwner
import mod.iceandshadow3.lib.item.BItemModelProperty
import mod.iceandshadow3.lib.util.E3vl

abstract class BLogicItem(dom: BDomain, baseName: String)
	extends BLogicWithItem(dom, baseName)
	with TEventFishOwner
	with BinderItem.TKey
{
	BinderItem.add(this)
	ContentLists.item.add(this)
	final override def pathPrefix: String = "item"
	final override def hasItem = true

	//TODO: Expand when we have our own text formatting stuff.
	def addTooltip(what: WItemStack) = ""
	def onUseGeneral(context: WUsageItem) = E3vl.NEUTRAL
	def onUseBlock(context: WUsageItemOnBlock) = E3vl.NEUTRAL
	def propertyOverrides: Array[BItemModelProperty] = new Array[BItemModelProperty](0)
	def getBurnTicks(stack: WItemStack) = 0

	def damageLimit = 0

	override def toWItem: WItemType = BinderItem.wrap(this)
	def getItemModelGen: Option[BJsonAssetGen[BLogicItem]] =
		Some(BJsonAssetGen.itemDefault)

	@Nullable def handlerTickOwned(held: Boolean): Consumer[WItemStackOwned[WEntity]] = null
	@Nullable def handlerShine: Predicate[WItemStack] = null
}

sealed abstract class BLogicItemSimple(dom: BDomain, name: String, override val tier: Int)
	extends BLogicItem(dom, name)

class LogicItemMulti(dom: BDomain, name: String, stacklimit: Int = 64, tier: Int = 1)
	extends BLogicItemSimple(dom, name, tier)
{
	override def stackLimit = stacklimit
	override final def damageLimit = 0
}

class LogicItemSingle(dom: BDomain, name: String, dmglimit: Int = 0, tier: Int = 1)
	extends BLogicItemSimple(dom, name, tier)
{
	override final def stackLimit = 1
	override def damageLimit = dmglimit
}
