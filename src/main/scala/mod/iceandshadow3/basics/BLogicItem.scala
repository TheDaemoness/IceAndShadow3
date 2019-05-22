package mod.iceandshadow3.basics

import mod.iceandshadow3.basics.item.BItemProperty
import mod.iceandshadow3.compat.CNbtTree
import mod.iceandshadow3.compat.entity.CRefPlayer
import mod.iceandshadow3.compat.item._
import mod.iceandshadow3.forge.fish.IEventFishItem
import mod.iceandshadow3.util.L3

sealed abstract class BLogicItem(dom: BDomain, name: String) extends BCompatLogicItem(dom, name) with IEventFishItem  {
	dom.add(this)
	/** Triggered when an EntityItem with an instance of an ItemStack controlled by this logic is about to spawn.
	 *  @return true to permit the spawn, false to deny it.
	 */
	def onDrop(variant: Int, stack: CRefItem): Boolean = true
	def isShiny(variant: Int, tags: CNbtTree, stack: CRefItem) = false
	
	def onUse(variant: Int, state: BStateData, stack: CRefItem, user: CRefPlayer, mainhand: Boolean): L3 = L3.NULL

	def propertyOverrides(): Array[BItemProperty] = new Array[BItemProperty](0)

	override def getLogic() = this
}

sealed abstract class BLogicItemSimple(dom: BDomain, name: String) extends BLogicItem(dom, name) {
	type StateDataType = BStateData
	override final def getDefaultStateData(variant: Int): BStateData = null
}
abstract class BLogicItemMulti(dom: BDomain, name: String) extends BLogicItemSimple(dom, name) {
	override def stackLimit(variant: Int) = 64
	override final def damageLimit(variant: Int) = 0
}
abstract class BLogicItemSingle(dom: BDomain, name: String) extends BLogicItemSimple(dom, name) {
	override final def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = 0
}

abstract class BLogicItemComplex(dom: BDomain, name: String) extends BLogicItem(dom, name) {
	//TODO: Investigate whether Minecraft actually knows how to handle stacking items with NBT.
	override final def stackLimit(variant: Int) = 1
	override def damageLimit(variant: Int) = 0
}
