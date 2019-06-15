package mod.iceandshadow3.world.nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.basics.item.BItemProperty
import mod.iceandshadow3.basics.util.LogicPair
import mod.iceandshadow3.basics.{BLogicItemComplex, BStateData}
import mod.iceandshadow3.compat.entity.{WEntityLiving, WEntityPlayer}
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.compat.misc.WNbtTree
import mod.iceandshadow3.compat.world.{TWWorld, WDimensionCoord, WSound}
import mod.iceandshadow3.data._
import mod.iceandshadow3.forge.fish.{TEventFishOwnerDeath, TEventFishOwnerToss}
import mod.iceandshadow3.spatial.{IVec3, PerDimensionVec3}
import mod.iceandshadow3.util.E3vl
import mod.iceandshadow3.world.misc.Statuses
import mod.iceandshadow3.world.{DimensionNyx, DomainNyx}

sealed class SIWayfinder extends BStateData {
	val charged = new DatumBool(false)
	register("charged", charged)
	val positions = new PerDimensionVec3
	register("positions", positions)
}
sealed abstract class BItemPropertyDelta(logic: BLogicItemComplex) extends BItemProperty(logic) {
	def evaluate(owner: WEntityLiving, point: Option[IVec3]): Float
	override def call(item: WItemStack, world: TWWorld): Float = {
		if(!item.hasOwner) return 0f
		val owner = item.getOwner
		val state = item.exposeStateData(new LogicPair(logic, 0)).asInstanceOf[SIWayfinder]
		evaluate(owner, state.positions.get(owner.dimensionCoord))
	}
}
class LIWayfinder extends BLogicItemComplex(DomainNyx, "wayfinder")
	with TEventFishOwnerDeath
	with TEventFishOwnerToss
{
	override def getTier(variant: Int): Int = 2
	protected def getDefaultCoord(who: WEntityLiving): IVec3 =
		who.home(who.dimension).getOrElse(who.position)

	override type StateDataType = SIWayfinder
	override def getDefaultStateData(variant: Int) = new SIWayfinder

	override def isShiny(variant: Int, tags: WNbtTree, stack: WItemStack) =
		tags.chroot(IaS3.MODID).getLong("charged") > 0
		
	override def onUse(variant: Int, state: SIWayfinder, stack: WItemStack, user: WEntityPlayer, mainhand: Boolean): E3vl = {
		stack.forStateData(state, ()=>{
			if (!mainhand) {
				if (!state.charged.get) {
					val found = user.findItem("minecraft:totem_of_undying", restrictToHands = true)
					if (!found.isEmpty) {
						found.consume()
						user.playSound(WSound.lookup("minecraft:item.totem.use"), 0.5f, 1f)
						state.charged.set(true)
						E3vl.TRUE
					} else E3vl.FALSE
				} else E3vl.FALSE
			} else if(user.sneaking) {
				state.positions.set(user.dimensionCoord, user.position)
				//TODO: More feedback.
				E3vl.TRUE
			} else E3vl.NEUTRAL
		})
	}

	def teleportItem(item: WItemStack): Boolean = {
		//item.getOwner.itemsStashed().foreach(_.destroy())
		item.getOwner.saveItem(item)
	}

	override def onOwnerDeath(variant: Int, s: BStateData, item: WItemStack, isCanceled: Boolean): E3vl = {
		val state = s.asInstanceOf[StateDataType]
		val result = item.forStateData(state, ()=> {
			val preventDeath = !isCanceled && state.charged.get
			val owner = item.getOwner
			if (preventDeath) {
				owner.setHp()
				owner.setStatus(Statuses.resistance, 160, 5)
				owner.extinguish()
				val where = state.positions.get(owner.dimensionCoord).getOrElse(owner.home(owner.dimension).orNull)
				if (where != null) {
					owner.teleport(where)
					owner match {
						case player: WEntityPlayer => player.advancement("vanilla_wayfinder_save")
						case _ =>
					}
					item.getOwner.playSound(WSound.lookup(
						"minecraft:item.chorus_fruit.teleport"
					), 1f, 0.9f)
				}
				state.charged.set(false)
			} else {
				state.positions.set(owner.dimensionCoord, owner.position)
			}
			E3vl.FALSE.unlessFalse(preventDeath)
		})
		teleportItem(item)
		result
	}

	override def onOwnerVoided(variant: Int, s: BStateData, item: WItemStack, isCanceled: Boolean) = {
		val state = s.asInstanceOf[StateDataType]
		val result = item.forStateData(state, ()=> {
			val owner = item.getOwner
			val preventDeath = !isCanceled && state.charged.get && owner.position.yBlock < -60
			if (preventDeath) {
				val areweinnyx = owner.dimensionCoord == DimensionNyx.coord
				owner match {
					case player: WEntityPlayer =>
						player.advancement("vanilla_outworlder")
						player.advancement(if(areweinnyx) "nyx_escape" else "nyx_root")
						//TODO: The nyx root advancement should not have to be manually triggered.
					case _ =>
				}
				owner.setHp(1)
				owner.setStatus(Statuses.resistance, 160, 5)
				if(areweinnyx) owner.teleport(WDimensionCoord.END)
				else owner.teleport(DimensionNyx)
				state.charged.set(false)
			}
			E3vl.FALSE.unlessFalse(preventDeath)
		})
		teleportItem(item)
		result
	}

	override def onOwnerToss(variant: Int, s: BStateData, item: WItemStack): E3vl = {
		val result = E3vl.FALSE.unlessFalse(teleportItem(item))
		if(result.isFalse) item.getOwner.playSound(WSound.lookup(
			"minecraft:item.chorus_fruit.teleport"
		), 0.5f, 1.1f)
		result
	}

	override def propertyOverrides() = Array(
		new BItemProperty(this) {
			override def name = "charged"
			override def call(item: WItemStack, world: TWWorld): Float =
				if(item.exposeNbtTree().chroot().getLong("charged") > 0) 1f else 0f
		},
		new BItemPropertyDelta(this) {
			override def name = "blink"
			override def evaluate(owner: WEntityLiving, point: Option[IVec3]): Float = {
				if(point.isEmpty) (1+(owner.gameTime & 31))/32f
				else 0f
			}
		},
		new BItemPropertyDelta(this) {
			override def name = "hotness"
			override def evaluate(owner: WEntityLiving, point: Option[IVec3]): Float = {
				if(point.isEmpty) 0f
				else {
					def angle = owner.facingH.angle(owner.position.delta(point.get))/Math.PI
					Math.max(0f, 1f - angle.abs.toFloat)
				}
			}
		}
	)
}