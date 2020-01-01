package mod.iceandshadow3.lib

import java.util.function.{Consumer, Predicate}

import javax.annotation.Nullable
import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.base.{BLogic, TLogicWithItem, TNamed}
import mod.iceandshadow3.lib.compat.WIdItem
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.BJsonGen
import mod.iceandshadow3.lib.compat.item._
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import mod.iceandshadow3.lib.forge.fish.TEventFishOwner
import mod.iceandshadow3.lib.item.BItemModelProperty
import mod.iceandshadow3.lib.util.E3vl

abstract class BLogicItem(dom: BDomain, baseName: String)
	extends BLogic(dom, baseName)
	with TLogicWithItem
	with TEventFishOwner
	with BinderItem.TKey
	with TNamed[WIdItem]
{
	BinderItem.add(this)
	ContentLists.item.add(this)
	final val id: WIdItem = new WIdItem(IaS3.MODID, domain.makeName(baseName))
	final override def pathPrefix: String = "item"
	final override def itemLogic = Some(this)

	def onUseGeneral(context: WUsageItem) = E3vl.NEUTRAL
	def onUseBlock(context: WUsageItemOnBlock) = E3vl.NEUTRAL
	def propertyOverrides: Array[BItemModelProperty] = new Array[BItemModelProperty](0)
	def getBurnTicks(stack: WItemStack) = 0

	def damageLimit = 0

	final override def toWItemType: WItemType = WItemType.make(this)
	final def toWItemStack: WItemStack = WItemStack.make(this)
	def getItemModelGen: Option[BJsonGen] =
		Some(BJsonGen.modelItemDefault(this))

	@Nullable def handlerTooltip: java.util.function.Function[WItemStack, String] = null
	@Nullable def handlerTickOwned(held: Boolean): Consumer[WItemStackOwned[WEntity]] = null
	@Nullable def handlerShine: Predicate[WItemStack] = null
}

sealed abstract class BLogicItemSimple(dom: BDomain, name: String, override val tier: Int)
	extends BLogicItem(dom, name)

class LogicItemMulti(dom: BDomain, name: String, tier: Int = 1, stacklimit: Int = 64)
	extends BLogicItemSimple(dom, name, tier)
{
	override def stackLimit = stacklimit
	override final def damageLimit = 0
}

class LogicItemSingle(dom: BDomain, name: String, tier: Int = 1, dmglimit: Int = 0)
	extends BLogicItemSimple(dom, name, tier)
{
	override final def stackLimit = 1
	override def damageLimit = dmglimit
}
