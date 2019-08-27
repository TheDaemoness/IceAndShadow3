package mod.iceandshadow3.lib

import mod.iceandshadow3.ContentLists
import mod.iceandshadow3.lib.item.BItemProperty
import mod.iceandshadow3.lib.compat.item._
import mod.iceandshadow3.lib.compat.item.impl.{BCompatLogicItem, BinderItem}
import mod.iceandshadow3.lib.compat.misc.WNbtTree
import mod.iceandshadow3.lib.forge.fish.TEventFishOwner
import mod.iceandshadow3.lib.util.E3vl

sealed abstract class BLogicItem(dom: BDomain, name: String)
	extends BCompatLogicItem(dom, name)
	with TEventFishOwner
	with BinderItem.TKey
{
	BinderItem.add(this)
	ContentLists.item.add(this)
	override def getPathPrefix: String = "item"

	//TODO: Expand when we have our own text formatting stuff.
	def addTooltip(variant: Int, what: WItemStack): String = ""
	def isShiny(variant: Int, tags: WNbtTree, stack: WItemStack) = false
	def onUseGeneral(variant: Int, context: WUsageItem) = E3vl.NEUTRAL
	def onUseBlock(variant: Int, context: WUsageItemOnBlock) = E3vl.NEUTRAL
	def propertyOverrides(): Array[BItemProperty] = new Array[BItemProperty](0)
	def getBurnTicks(variant: Int, stack: WItemStack) = 0
	override def resistsExousia(variant: Int) = false
}

sealed abstract class BLogicItemSimple(dom: BDomain, name: String, variants: Seq[(String, Int)])
	extends BLogicItem(dom, name)
{
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int): BStateData = null

	override def countVariants = variants.size
	override protected def getVariantName(variant: Int) = variants(variant)._1
	override def getTier(variant: Int) = variants(variant)._2
}

class LogicItemMulti(dom: BDomain, name: String, variants: (String, Int)*)
	extends BLogicItemSimple(dom, name, variants)
{
	def this(dom: BDomain, name: String, tier: Int) = this(dom, name, (null, tier))
	override def stackLimit(variant: Int) = 64
	override final def damageLimit(variant: Int) = 0
}

class LogicItemSingle(dom: BDomain, name: String, variants: (String, Int)*)
	extends BLogicItemSimple(dom, name, variants)
{
	def this(dom: BDomain, name: String, tier: Int) = this(dom, name, (null, tier))
	override final def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = 0
}

abstract class BLogicItemComplex(dom: BDomain, name: String) extends BLogicItem(dom, name) {
	//TODO: Investigate whether Minecraft actually knows how to handle stacking items with NBT.
	override def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = 0
}
