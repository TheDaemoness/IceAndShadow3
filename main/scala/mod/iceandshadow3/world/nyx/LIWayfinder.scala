package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.BLogicItemComplex
import mod.iceandshadow3.basics.BStateData
import mod.iceandshadow3.compat.CNbtTree
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.entity.CRefPlayer
import mod.iceandshadow3.data._
import mod.iceandshadow3.util.L3
import mod.iceandshadow3.world.DomainNyx

sealed class SIWayfinder extends BStateData {
	var charged = new DatumBool(false)
	register("charged", charged)
}
class LIWayfinder extends BLogicItemComplex(DomainNyx, "wayfinder") {
	override type StateDataType = SIWayfinder
	override def getDefaultStateData(variant: Int) = new SIWayfinder
	
	override def isShiny(variant: Int, tags: CNbtTree, stack: CRefItem) =
		tags.chroot(IaS3.MODID).getLong("charged") > 0
		
	override def onUse(variant: Int, state: BStateData, stack: CRefItem, user: CRefPlayer, mainhand: Boolean): L3 = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		if(!mainhand && !wayfinderstate.charged.get) {
			val found = user.findItem("minecraft:totem_of_undying", true)
			if(!found.isEmpty) {
				found.consume()
				wayfinderstate.charged.set(true)
				return L3.TRUE
			}
		}
		L3.NULL //TODO: Position setting.
	}
}