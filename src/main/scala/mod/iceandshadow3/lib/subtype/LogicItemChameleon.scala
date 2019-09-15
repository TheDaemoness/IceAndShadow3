package mod.iceandshadow3.lib.subtype

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.misc.TextUtils
import mod.iceandshadow3.lib.compat.nbt.{VarNbtItemStack, VarNbtString}
import mod.iceandshadow3.lib.{BDomain, BLogicItemComplex, BStateData}

/** An item that pretends to be whole stacks of other items.
	*/
class LogicItemChameleon(domain: BDomain, name: String) extends BLogicItemComplex(domain, name) {
	override type StateDataType = BStateData
	override def getDefaultStateData(variant: Int) = null
	override def getTier(variant: Int) = 1
	override def isTechnical = true

	override def nameOverride(variant: Int, what: WItemStack) = TextUtils.itemIdToUnlocalized(
		what(LogicItemChameleon.varItemWrappedName)
	)
}
object LogicItemChameleon {
	val varItemWrappedName: VarNbtString = new VarNbtString("id", "minecraft:snowball") {
		override val path = super.path.appended("itemstack")
	}
	val varItemWrapped = new VarNbtItemStack("itemstack")
	def createFrom(what: WItemStack, logic: LogicItemChameleon): WItemStack = {
		val base = WItemStack.make(logic, 0)
		base(varItemWrapped) = what
		base
	}
}
