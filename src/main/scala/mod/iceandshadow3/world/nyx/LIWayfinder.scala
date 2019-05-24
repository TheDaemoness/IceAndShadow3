package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.{BLogicItemComplex, BStateData}
import mod.iceandshadow3.compat.CNbtTree
import mod.iceandshadow3.compat.entity.{CRefLiving, CRefPlayer}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.{CSound, PerDimensionVec3}
import mod.iceandshadow3.data._
import mod.iceandshadow3.forge.fish.{IEventFishOwnerDeath, IEventFishOwnerToss}
import mod.iceandshadow3.util.{L3, Vec3}
import mod.iceandshadow3.world.DomainNyx

sealed class SIWayfinder extends BStateData {
	val charged = new DatumBool(false)
	register("charged", charged)
	val positions = new PerDimensionVec3
	register("positions", positions)
}
class LIWayfinder extends BLogicItemComplex(DomainNyx, "wayfinder")
	with IEventFishOwnerDeath
	with IEventFishOwnerToss
{
	protected def getDefaultCoord(who: CRefLiving): Vec3 =
		who.home(who.dimension).getOrElse(who.position)

	override type StateDataType = SIWayfinder
	override def getDefaultStateData(variant: Int) = new SIWayfinder

	override def isShiny(variant: Int, tags: CNbtTree, stack: CRefItem) =
		tags.chroot(IaS3.MODID).getLong("charged") > 0
		
	override def onUse(variant: Int, state: BStateData, stack: CRefItem, user: CRefPlayer, mainhand: Boolean): L3 = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		stack.forStateData(wayfinderstate, ()=>{
			if (!mainhand) {
				if (!wayfinderstate.charged.get) {
					val found = user.findItem("minecraft:totem_of_undying", true)
					if (!found.isEmpty) {
						found.consume()
						user.playSound(CSound.lookup("minecraft:item.totem.use"), 0.5f, 1f)
						wayfinderstate.charged.set(true)
						L3.TRUE
					} else L3.FALSE
				} else L3.FALSE
			} else {
				wayfinderstate.positions.set(user.dimensionCoord, user.position)
				//TODO: More feedback. Maybe make this have a wind-up.
				L3.TRUE
			}
		})
	}

	override def onOwnerDeath(variant: Int, state: BStateData, item: CRefItem, isCancelled: Boolean): L3 = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		val result = item.forStateData(wayfinderstate, ()=> {
			val preventDeath = !isCancelled && wayfinderstate.charged.get
			val owner = item.getOwner
			if (preventDeath) {
				owner.setHp()
				owner.extinguish()
				val where = wayfinderstate.positions.get(owner.dimensionCoord).getOrElse(owner.home(owner.dimension).orNull)
				if (where != null) {
					owner.teleport(where)
					item.getOwner.playSound(CSound.lookup(
						"minecraft:item.chorus_fruit.teleport"
					), 1f, 0.9f)
				}
				wayfinderstate.charged.set(false)
			} else {
				wayfinderstate.positions.set(owner.dimensionCoord, owner.position)
			}
			L3.FALSE.unlessFalse(preventDeath)
		})
		item.getOwner.saveItem(item)
		result
	}
	override def onOwnerToss(variant: Int, state: BStateData, item: CRefItem): L3 = {
		val result = L3.FALSE.unlessFalse(item.getOwner.saveItem(item))
		if(result == L3.FALSE) item.getOwner.playSound(CSound.lookup(
			"minecraft:item.chorus_fruit.teleport"
		), 0.5f, 1.1f)
		result
	}
}