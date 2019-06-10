package mod.iceandshadow3.basics

import mod.iceandshadow3.basics.item.BItemProperty
import mod.iceandshadow3.compat.WNbtTree
import mod.iceandshadow3.compat.entity.WEntityPlayer
import mod.iceandshadow3.compat.item._
import mod.iceandshadow3.forge.fish.IEventFishOwner
import mod.iceandshadow3.util.E3vl

sealed abstract class BLogicItem(dom: BDomain, name: String)
	extends BCompatLogicItem(dom, name)
	with IEventFishOwner
	with BinderItem.TKey
{
	BinderItem.add(this)

	def isShiny(variant: Int, tags: WNbtTree, stack: WItemStack) = false
	def onUse(variant: Int, state: BStateData, stack: WItemStack, user: WEntityPlayer, mainhand: Boolean): E3vl = E3vl.NEUTRAL
	def propertyOverrides(): Array[BItemProperty] = new Array[BItemProperty](0)
	override def getLogic() = this
	override def resistsExousia(variant: Int) = false
}

sealed abstract class BLogicItemSimple(dom: BDomain, name: String) extends BLogicItem(dom, name) {
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int): BStateData = null
}
class LogicItemMulti(dom: BDomain, name: String) extends BLogicItemSimple(dom, name) {
	override def stackLimit(variant: Int) = 64
	override final def damageLimit(variant: Int) = 0
}
class LogicItemSingle(dom: BDomain, name: String) extends BLogicItemSimple(dom, name) {
	override final def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = 0
}

abstract class BLogicItemComplex(dom: BDomain, name: String) extends BLogicItem(dom, name) {
	//TODO: Investigate whether Minecraft actually knows how to handle stacking items with NBT.
	override final def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = 0
}
