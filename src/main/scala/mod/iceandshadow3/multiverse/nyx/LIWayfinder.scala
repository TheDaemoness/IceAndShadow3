package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.lib.compat.entity.{WEntityLiving, WEntityPlayerReal}
import mod.iceandshadow3.lib.compat.item.{WItemStack, WUsageItem}
import mod.iceandshadow3.lib.compat.nbt.{VarNbtBool, VarNbtObj}
import mod.iceandshadow3.lib.compat.world.{TWWorld, WDimensionCoord, WSound}
import mod.iceandshadow3.lib.forge.fish.{TEventFishOwnerDeath, TEventFishOwnerToss}
import mod.iceandshadow3.lib.item.BItemProperty
import mod.iceandshadow3.lib.spatial.{IVec3, PerDimensionVec3}
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.lib.LogicItemSingle
import mod.iceandshadow3.multiverse.misc.StatusEffects
import mod.iceandshadow3.multiverse.{DimensionNyx, DomainNyx}

object LIWayfinder {
	val varCharged = new VarNbtBool("charged", false)
	val varPos = new VarNbtObj("positions", new PerDimensionVec3)

	sealed abstract class BItemPropertyDelta(logic: LogicItemSingle) extends BItemProperty(logic) {
		def evaluate(owner: WEntityLiving, point: Option[IVec3]): Float
		override def call(item: WItemStack, world: TWWorld): Float = {
			if(!item.hasOwner) return 0f
			val owner = item.getOwner
			evaluate(owner, item(LIWayfinder.varPos).get(owner.dimensionCoord))
		}
	}

	val teleportProtection = StatusEffects.resistance.forTicks(159, 5)
}
class LIWayfinder extends LogicItemSingle(DomainNyx, "wayfinder", 2)
	with TEventFishOwnerDeath
	with TEventFishOwnerToss
{
	protected def getDefaultCoord(who: WEntityLiving): IVec3 =
		who.home(who.dimension).getOrElse(who.posFine)

	override def isShiny(variant: Int, stack: WItemStack) =
		stack(LIWayfinder.varCharged)

	override def onUseGeneral(variant: Int, context: WUsageItem): E3vl = {
		if (!context.mainhand) {
			if (!context.stack(LIWayfinder.varCharged)) {
				val found = context.user.findItem("minecraft:totem_of_undying", restrictToHands = true)
				if (!found.isEmpty) {
					found.consume()
					context.user.advancement("vanilla_wayfinder_charged")
					context.user.playSound(WSound("minecraft:item.totem.use"), 0.5f, 1f)
					context.stack(LIWayfinder.varCharged) = true
					E3vl.TRUE
				} else E3vl.FALSE
			} else E3vl.FALSE
		} else if(context.sneaking) {
			context.stack.transform[PerDimensionVec3](LIWayfinder.varPos, _.update(
				context.user.dimensionCoord,
				context.user.posFine
			))
			//TODO: More feedback.
			E3vl.TRUE
		} else E3vl.NEUTRAL
	}

	def teleportItem(item: WItemStack): Boolean = {
		//item.getOwner.itemsStashed().foreach(_.destroy())
		item.getOwner.saveItem(item)
	}

	override def onOwnerDeath(variant: Int, item: WItemStack, isCanceled: Boolean): E3vl = {
		val preventDeath = !isCanceled && item(LIWayfinder.varCharged)
		val owner = item.getOwner
		if (preventDeath) {
			owner.setHp()
			LIWayfinder.teleportProtection(owner)
			owner.extinguish()
			val where = item(LIWayfinder.varPos).get(owner.dimensionCoord).getOrElse(owner.home(owner.dimension).orNull)
			item.update(LIWayfinder.varCharged, false)
			if (where != null) {
				owner.teleport(where)
				item.getOwner.playSound(WSound("minecraft:item.chorus_fruit.teleport"), 1f, 0.9f)
			}
		} else {
			item.transform[PerDimensionVec3](LIWayfinder.varPos, _.update(owner.dimensionCoord, owner.posFine))
		}
		teleportItem(item)
		E3vl.FALSE.unlessFalse(preventDeath)
	}

	override def onOwnerVoided(variant: Int, item: WItemStack, isCanceled: Boolean) = {
		val owner = item.getOwner
		val preventDeath = !isCanceled && item(LIWayfinder.varCharged) && owner.posFine.yBlock < -60
		if (preventDeath) {
			val areweinnyx = owner.dimensionCoord == DimensionNyx.coord
			owner.setHp(1)
			LIWayfinder.teleportProtection(owner)
			item.update(LIWayfinder.varCharged, false)
			owner match {
				case player: WEntityPlayerReal =>
					player.advancement("vanilla_outworlder")
					if(areweinnyx) player.teleport(WDimensionCoord.END, null)
					else player.teleport(DimensionNyx)
				case _ =>
			}
		}
		teleportItem(item)
		E3vl.FALSE.unlessFalse(preventDeath)
	}

	override def onOwnerToss(variant: Int, item: WItemStack): E3vl = {
		val result = E3vl.FALSE.unlessFalse(teleportItem(item))
		if(result.isFalse) item.getOwner.playSound(WSound("minecraft:item.chorus_fruit.teleport"), 0.5f, 1.1f)
		result
	}

	override def propertyOverrides() = Array(
		new BItemProperty(this) {
			override def name = "charged"
			override def call(item: WItemStack, world: TWWorld): Float =
				if(item(LIWayfinder.varCharged)) 1f else 0f
		},
		new LIWayfinder.BItemPropertyDelta(this) {
			override def name = "blink"
			override def evaluate(owner: WEntityLiving, point: Option[IVec3]): Float = {
				if(point.isEmpty) (1+(owner.gameTime & 31))/32f
				else 0f
			}
		},
		new LIWayfinder.BItemPropertyDelta(this) {
			override def name = "hotness"
			override def evaluate(owner: WEntityLiving, point: Option[IVec3]): Float = {
				if(point.isEmpty) 0f
				else {
					def angle = owner.facingH.angle(owner.posFine.delta(point.get))/Math.PI
					Math.max(0f, 1f - angle.abs.toFloat)
				}
			}
		}
	)

	override def getItemModelGen(variant: Int) = None
}