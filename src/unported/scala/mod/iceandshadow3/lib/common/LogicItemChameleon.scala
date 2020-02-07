package mod.iceandshadow3.lib.common

import mod.iceandshadow3.lib.compat.id.WIdItem
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.nbt.{VarNbtItemStack, VarNbtString}
import mod.iceandshadow3.lib.{Domain, LogicItemSingle}

/** An item that "wraps" entire item stacks. */
class LogicItemChameleon(domain: Domain, name: String) extends LogicItemSingle(domain, name, 1) {
	override def isTechnical = true

	//TODO: Once we have our own formatted text class, get an instance from what(LogicItemChameleon.varItemWrapped)
	override def nameOverride(what: WItemStack) = WIdItem(
		what(LogicItemChameleon.varItemWrappedName)
	).translationKey
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
