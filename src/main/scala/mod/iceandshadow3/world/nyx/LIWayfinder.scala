package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.{BLogicItemComplex, BStateData}
import mod.iceandshadow3.compat.CNbtTree
import mod.iceandshadow3.compat.entity.CRefPlayer
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.PerDimensionVec3
import mod.iceandshadow3.data._
import mod.iceandshadow3.forge.fish.IEventFishOwnerDeath
import mod.iceandshadow3.util.L3
import mod.iceandshadow3.world.DomainNyx

sealed class SIWayfinder extends BStateData {
	val charged = new DatumBool(false)
	register("charged", charged)
	val positions = new PerDimensionVec3
	register("positions", positions)
}
class LIWayfinder extends BLogicItemComplex(DomainNyx, "wayfinder")
with IEventFishOwnerDeath
{
	override type StateDataType = SIWayfinder
	override def getDefaultStateData(variant: Int) = new SIWayfinder

	override def isShiny(variant: Int, tags: CNbtTree, stack: CRefItem) =
		tags.chroot(IaS3.MODID).getLong("charged") > 0
		
	override def onUse(variant: Int, state: BStateData, stack: CRefItem, user: CRefPlayer, mainhand: Boolean): L3 = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		if(!mainhand) {
			if(!wayfinderstate.charged.get) {
				val found = user.findItem("minecraft:totem_of_undying", true)
				if (!found.isEmpty) {
					found.consume()
					//TODO: More feedback.
					wayfinderstate.charged.set(true)
					return L3.TRUE
				}
			}
			return L3.FALSE
		} else {
			wayfinderstate.positions.set(user.dimensionCoord, user.position)
			//TODO: More feedback. Maybe make this have a wind-up.
			return L3.TRUE
		}
	}

	override def onOwnerDeath(variant: Int, state: BStateData, item: CRefItem, isCancelled: Boolean): L3 = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		val preventDeath = !isCancelled && wayfinderstate.charged.get
		if(preventDeath) {
			val owner = item.getOwner
			owner.setHp()
			val where = wayfinderstate.positions.get(owner.dimensionCoord).getOrElse(owner.home.orNull)
			if(where != null) owner.teleport(where)
			wayfinderstate.charged.set(false)
		}
		//TODO: Give to ender chest inventory.
		if(preventDeath) L3.FALSE else L3.NULL
	}
	//TODO: Upon the owner trying to change dimension, give to the ender chest inventory.
}