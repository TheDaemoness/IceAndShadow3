package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.compat.entity.CNVEntity
import mod.iceandshadow3.compat.item
import mod.iceandshadow3.compat.item.{WContainer, WItemStack}
import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeCold, TDmgTypeShadow}
import mod.iceandshadow3.util.MathUtils
import mod.iceandshadow3.world.misc.Statuses
import mod.iceandshadow3.world.DimensionNyx
import mod.iceandshadow3.world.dim_nyx.LIFrozen
import net.minecraft.entity.item.ItemEntity
import net.minecraft.util.{ActionResult, ActionResultType}
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.event.entity.player.PlayerContainerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerNyx extends BEventHandler {
	//TODO: Most of this is stretching The Rule. Fix if/when IaS3 adds more than one dimension.

	//CRITICAL: Make sure each of these have dimension checks!

	val placesHighAttack = new Attack("windchill", AttackForm.VOLUME, new BDamage with TDmgTypeCold {
		override def baseDamage = 1f

		override def onDamage(dmg: Float, dmgResisted: Float, what: WItemStack) = dmgResisted
	})
	val placesDarkAttack = new Attack("darkness", AttackForm.CURSE, new BDamage with TDmgTypeShadow {
		override def baseDamage = 4f

		override def onDamage(dmg: Float, dmgResisted: Float, what: WItemStack) = dmgResisted
	})

	@SubscribeEvent
	def onPoorInnocentSoulUpdate(victim: LivingUpdateEvent): Unit = {
		if (victim.getEntityLiving.dimension != DimensionNyx.coord.dimtype) return
		val who = CNVEntity.wrap(victim.getEntityLiving)
		if (who.getShadowPresence >= 1f) {
			who.setStatus(Statuses.blind, 55)
			who.damage(placesDarkAttack)
		}
		val height = who.position.yBlock
		if (height >= 192) who.damageWithStatus(
			placesHighAttack,
			4f - MathUtils.attenuateThrough(192, height, 255) * 3f,
			Statuses.frost, 115
		)
	}

	@SubscribeEvent
	def onPlayerExposesContainerToTheElements(oops: PlayerContainerEvent.Open): Unit = {
		val container = new WContainer(oops.getContainer)
		val player = CNVEntity.wrap(oops.getEntityPlayer)
		if (player.dimensionCoord == DimensionNyx.coord) DimensionNyx.freezeItems(container, player)
	}

	@SubscribeEvent
	def onInteract(event: PlayerInteractEvent): Unit = {
		//Countermeasure against having another way of obtaining a banned item and using it.
		val what = new WItemStack(event.getItemStack, event.getEntityPlayer)
		val frozen = LIFrozen.freeze(what, Some(CNVEntity.wrap(event.getEntityPlayer)))
		if(frozen.isDefined) {
			event.setCancellationResult(ActionResultType.FAIL)
			event.getEntityPlayer.setHeldItem(event.getHand, frozen.get.exposeItems())
		}
	}

	@SubscribeEvent
	def onEntityItemJoin(event: EntityJoinWorldEvent): Unit =
		if (event.getWorld.getDimension.getType == DimensionNyx.coord.dimtype) {
			event.getEntity match {
				case ei: ItemEntity =>
					val initial = new WItemStack(ei.getItem, null)
					val frozen = LIFrozen.freeze(initial, None)
					frozen.foreach(newitems => {
						if(newitems.isEmpty) event.setCanceled(true)
						else ei.setItem(newitems.exposeItems())
					})
				case _ =>
			}
		}
}
