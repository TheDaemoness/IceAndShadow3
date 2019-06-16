package mod.iceandshadow3.basics.common

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.{BDomain, BLogicItemComplex, BStateData}
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.misc.TextUtils

/** An item that pretends to be whole stacks of other items.
	*/
class LogicItemChameleon(domain: BDomain, name: String) extends BLogicItemComplex(domain, name) {
	override type StateDataType = BStateData
	override def getDefaultStateData(variant: Int) = null
	override def getTier(variant: Int) = 1
	override def isTechnical = true

	override def nameOverride(variant: Int, what: WItemStack) = TextUtils.itemIdToUnlocalized(
		what.exposeNbtTree().chroot(IaS3.MODID).chroot("itemstack").getString("id")
	)
}
object LogicItemChameleon {
	def createFrom(what: WItemStack, logic: LogicItemChameleon): WItemStack = {
		val base = WItemStack.make(logic, 0)
		base.exposeNbtTree().chroot(IaS3.MODID).set("itemstack", what)
		base
	}
}