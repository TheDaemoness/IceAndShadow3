package mod.iceandshadow3.lib

import java.util.function.{Consumer, Predicate}

import javax.annotation.Nullable
import mod.iceandshadow3.{ContentLists, IaS3}
import mod.iceandshadow3.lib.base.{LogicCommon, TLogicWithItem, TNamed}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.file.JsonGen
import mod.iceandshadow3.lib.compat.item._
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import mod.iceandshadow3.lib.compat.forge.fish.TEventFishOwner
import mod.iceandshadow3.lib.compat.id.WIdItem
import mod.iceandshadow3.lib.item.ItemModelProperty
import mod.iceandshadow3.lib.util.E3vl

abstract class BLogicItem(dom: Domain, baseName: String)
	extends LogicCommon(dom, baseName)
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
	def propertyOverrides: Array[ItemModelProperty] = new Array[ItemModelProperty](0)
	def getBurnTicks(stack: WItemStack) = 0

	def damageLimit = 0

	final override def toWItemType: WItemType = WItemType.make(this)
	final def toWItemStack: WItemStack = WItemStack.make(this)
	def getItemModelGen: Option[JsonGen] =
		Some(JsonGen.modelItemDefault(this))

	@Nullable def handlerTooltip: java.util.function.Function[WItemStack, String] = null
	@Nullable def handlerTickOwned(held: Boolean): Consumer[WItemStackOwned[WEntity]] = null
	@Nullable def handlerShine: Predicate[WItemStack] = null
}

class LogicItemMulti(dom: Domain, name: String, override val tier: Int = 1, stacklimit: Int = 64)
	extends BLogicItem(dom, name)
{
	override def stackLimit = stacklimit
	override final def damageLimit = 0
}

class LogicItemSingle(dom: Domain, name: String, override val tier: Int = 1, dmglimit: Int = 0)
	extends BLogicItem(dom, name)
{
	override final def stackLimit = 1
	override def damageLimit = dmglimit
}
