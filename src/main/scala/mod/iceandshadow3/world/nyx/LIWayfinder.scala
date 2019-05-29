package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.item.BItemProperty
import mod.iceandshadow3.basics.util.LogicPair
import mod.iceandshadow3.basics.{BLogicItemComplex, BStateData}
import mod.iceandshadow3.compat.CNbtTree
import mod.iceandshadow3.compat.dimension.CDimensionCoord
import mod.iceandshadow3.compat.entity.{CRefLiving, CRefPlayer}
import mod.iceandshadow3.compat.item.CRefItem
import mod.iceandshadow3.compat.world.{CSound, TCWorld}
import mod.iceandshadow3.data._
import mod.iceandshadow3.forge.fish.{IEventFishOwnerDeath, IEventFishOwnerToss}
import mod.iceandshadow3.util.L3
import mod.iceandshadow3.spatial.{IVec3, PerDimensionVec3}
import mod.iceandshadow3.world.{DimensionNyx, DomainNyx}

sealed class SIWayfinder extends BStateData {
	val charged = new DatumBool(false)
	register("charged", charged)
	val positions = new PerDimensionVec3
	register("positions", positions)
}
sealed abstract class BItemPropertyDelta(logic: BLogicItemComplex) extends BItemProperty(logic) {
	def evaluate(owner: CRefLiving, point: Option[IVec3]): Float
	override def call(item: CRefItem, world: TCWorld): Float = {
		if(!item.hasOwner) return 0f
		val owner = item.getOwner
		val state = item.exposeStateData(new LogicPair(logic, 0)).asInstanceOf[SIWayfinder]
		evaluate(owner, state.positions.get(owner.dimensionCoord))
	}
}
class LIWayfinder extends BLogicItemComplex(DomainNyx, "wayfinder")
	with IEventFishOwnerDeath
	with IEventFishOwnerToss
{
	protected def getDefaultCoord(who: CRefLiving): IVec3 =
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
			} else if(user.sneaking) {
				wayfinderstate.positions.set(user.dimensionCoord, user.position)
				//TODO: More feedback.
				L3.TRUE
			} else L3.NEUTRAL
		})
	}

	def teleportItem(item: CRefItem): Boolean = {
		//item.getOwner.itemsStashed().foreach(_.destroy())
		item.getOwner.saveItem(item)
	}

	override def onOwnerDeath(variant: Int, state: BStateData, item: CRefItem, isCanceled: Boolean): L3 = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		val result = item.forStateData(wayfinderstate, ()=> {
			val preventDeath = !isCanceled && wayfinderstate.charged.get
			val owner = item.getOwner
			if (preventDeath) {
				owner.setHp()
				owner.extinguish()
				val where = wayfinderstate.positions.get(owner.dimensionCoord).getOrElse(owner.home(owner.dimension).orNull)
				if (where != null) {
					owner.teleport(where)
					owner match {
						case player: CRefPlayer => player.advancement("vanilla_wayfinder_save")
						case _ =>
					}
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
		teleportItem(item)
		result
	}

	override def onOwnerVoided(variant: Int, state: BStateData, item: CRefItem, isCanceled: Boolean) = {
		val wayfinderstate = state.asInstanceOf[SIWayfinder]
		val result = item.forStateData(wayfinderstate, ()=> {
			val owner = item.getOwner
			val preventDeath = !isCanceled && wayfinderstate.charged.get && owner.position.yBlock < -60
			if (preventDeath) {
				val areweinnyx = owner.dimensionCoord == DimensionNyx.coord
				owner match {
					case player: CRefPlayer =>
						player.advancement("vanilla_outworlder")
						player.advancement(if(areweinnyx) "nyx_escape" else "nyx_root")
						//TODO: The nyx root advancement should not have to be manually triggered.
					case _ =>
				}
				owner.setHp(1)
				if(areweinnyx) owner.teleportVanilla(CDimensionCoord.END)
				else owner.teleport(DimensionNyx)
				wayfinderstate.charged.set(false)
			}
			L3.FALSE.unlessFalse(preventDeath)
		})
		teleportItem(item)
		result
	}

	override def onOwnerToss(variant: Int, state: BStateData, item: CRefItem): L3 = {
		val result = L3.FALSE.unlessFalse(teleportItem(item))
		if(result == L3.FALSE) item.getOwner.playSound(CSound.lookup(
			"minecraft:item.chorus_fruit.teleport"
		), 0.5f, 1.1f)
		result
	}

	override def propertyOverrides() = Array(
		new BItemProperty(this) {
			override def name = "charged"
			override def call(item: CRefItem, world: TCWorld): Float =
				if(item.exposeNbtTree().chroot().getLong("charged") > 0) 1f else 0f
		},
		new BItemPropertyDelta(this) {
			override def name = "blink"
			override def evaluate(owner: CRefLiving, point: Option[IVec3]): Float = {
				if(point.isEmpty) (1+(owner.gameTime & 31))/32f
				else 0f
			}
		},
		new BItemPropertyDelta(this) {
			override def name = "hotness"
			override def evaluate(owner: CRefLiving, point: Option[IVec3]): Float = {
				if(point.isEmpty) 0f
				else {
					def angle = owner.facingH.angle(owner.position.delta(point.get))/Math.PI
					Math.max(0f, 1f - angle.abs.toFloat)
				}
			}
		}
	)
}