package mod.iceandshadow3.lib

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

abstract class BLogicItem(dom: BDomain, name: String)
	extends BLogicWithItem(dom, name)
	with TEventFishOwner
	with BinderItem.TKey
{
	BinderItem.add(this)
	ContentLists.item.add(this)
	final override def getPathPrefix: String = "item"
	final override def hasItem(variant: Int): Boolean = true

	//TODO: Expand when we have our own text formatting stuff.
	def addTooltip(variant: Int, what: WItemStack): String = ""
	def isShiny(variant: Int, stack: WItemStack) = false
	def onUseGeneral(variant: Int, context: WUsageItem) = E3vl.NEUTRAL
	def onUseBlock(variant: Int, context: WUsageItemOnBlock) = E3vl.NEUTRAL
	def propertyOverrides(): Array[BItemModelProperty] = new Array[BItemModelProperty](0)
	def getBurnTicks(variant: Int, stack: WItemStack) = 0

	def damageLimit(variant: Int) = 0

	override def asWItem(variant: Int = 0) = BinderItem.wrap(this, variant)
	def getItemModelGen(variant: Int): Option[BJsonAssetGen[BLogicItem]] =
		Some(BJsonAssetGen.itemDefault)

	@Nullable def handlerTickOwned(variant: Int, held: Boolean): WItemStackOwned[WEntity] => Unit = null
}

sealed abstract class BLogicItemSimple(dom: BDomain, name: String, variants: (String, Int)*)
	extends BLogicItem(dom, name)
{
	override def countVariants = variants.size
	override protected def getVariantName(variant: Int) = variants(variant)._1
	override def getTier(variant: Int) = variants(variant)._2
}

class LogicItemMulti(dom: BDomain, name: String, stacklimit: Int, variants: (String, Int)*)
	extends BLogicItemSimple(dom, name, variants:_*)
{
	def this(dom: BDomain, name: String, variants: (String, Int)*) = this(dom, name, 64, variants:_*)
	def this(dom: BDomain, name: String, stacklimit: Int, tier: Int) = this(dom, name, stacklimit, (null, tier))
	def this(dom: BDomain, name: String, tier: Int) = this(dom, name, 64, (null, tier))
	override def stackLimit(variant: Int) = stacklimit
	override final def damageLimit(variant: Int) = 0
}

class LogicItemSingle(dom: BDomain, name: String, dmglimit: Int, variants: (String, Int)*)
	extends BLogicItemSimple(dom, name, variants:_*)
{
	def this(dom: BDomain, name: String, variants: (String, Int)*) = this(dom, name, 0, variants:_*)
	def this(dom: BDomain, name: String, dmglimit: Int, tier: Int) = this(dom, name, dmglimit, (null, tier))
	def this(dom: BDomain, name: String, tier: Int) = this(dom, name, 0, (null, tier))
	override final def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = dmglimit
}
