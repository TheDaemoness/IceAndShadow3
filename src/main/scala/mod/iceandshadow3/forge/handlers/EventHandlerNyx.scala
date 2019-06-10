package mod.iceandshadow3.forge.handlers

import mod.iceandshadow3.compat.entity.CNVEntity
import mod.iceandshadow3.compat.item.WItemStack
import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeCold, TDmgTypeShadow}
import mod.iceandshadow3.util.MathUtils
import mod.iceandshadow3.world.misc.Statuses
import mod.iceandshadow3.world.{DimensionNyx, DomainNyx}
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class EventHandlerNyx extends BEventHandler {
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
		if(victim.getEntityLiving.dimension != DimensionNyx.coord.dimtype) return
		val who = CNVEntity.wrap(victim.getEntityLiving)
		if(who.getShadowPresence >= 1f) {
			who.setStatus(Statuses.blind, 55)
			who.damage(placesDarkAttack)
		}
		val height = who.position.yBlock
		if(height >= 192) {
			who.setStatus(Statuses.frost, 95)
			who.damage(placesHighAttack, 4f-MathUtils.attenuateThrough(192, height, 255)*3)
		}
	}
}
