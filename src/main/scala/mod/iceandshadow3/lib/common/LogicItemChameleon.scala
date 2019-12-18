package mod.iceandshadow3.lib.common

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.misc.TextUtils
import mod.iceandshadow3.lib.compat.nbt.{VarNbtItemStack, VarNbtString}
import mod.iceandshadow3.lib.{BDomain, BLogicItem}

/** An item that pretends to be whole stacks of other items.
	*/
class LogicItemChameleon(domain: BDomain, name: String) extends BLogicItem(domain, name) {
	override def tier = 1
	override def isTechnical = true

	override def nameOverride(what: WItemStack) = TextUtils.itemIdToUnlocalized(
		what(LogicItemChameleon.varItemWrappedName)
	)

	override def stackLimit = 1
}
object LogicItemChameleon {
	val varItemWrappedName: VarNbtString = new VarNbtString("id", "minecraft:snowball") {
		override val path = super.path.appended("itemstack")
	}
	val varItemWrapped = new VarNbtItemStack("itemstack")
	def createFrom(what: WItemStack, logic: LogicItemChameleon): WItemStack = {
		val base = WItemStack.make(logic)
		base(varItemWrapped) = what
		base
	}
}
