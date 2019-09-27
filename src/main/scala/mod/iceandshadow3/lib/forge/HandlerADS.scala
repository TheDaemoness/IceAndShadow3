package mod.iceandshadow3.lib.forge

import mod.iceandshadow3.damage.{AdsArmorValue, TDmgTypeNatural}
import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.entity.state.EquipPoint
import mod.iceandshadow3.lib.compat.entity.state.impl.ADamageSource
import mod.iceandshadow3.lib.util.CollectUtils
import mod.iceandshadow3.multiverse.misc.Statuses
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.eventbus.api.EventPriority

object HandlerADS {
	def registerSelf(): Unit = {
		MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, handle)
	}
	def handle(event: LivingDamageEvent): Unit = event.getSource match {
		case ads: ADamageSource =>
			val attack = ads.getAdsAttack
			val victim = CNVEntity.wrap(event.getEntityLiving)
			val base = ads.getTotal
			if(event.getAmount <= 0f || base <= 0f) event.setCanceled(true)
			else {
				val mult = event.getAmount / base
				val equipset = CollectUtils.randomPick(attack.form.relevantArmor.toIndexedSeq, victim.rng())
				var finaldamage = 0f
				for(idx <- attack.instances.indices) {
					val damage = attack.instances(idx)
					var avgdamage = 0f
					val baseDmg = ads.getAmount(idx) * mult
					//Compute contributions from a shield/auras.
					val totalShieldValue = if(attack.form.blockable) {
						AdsArmorValue(damage, AdsArmorValue.getFromShield(victim.equipment(EquipPoint.USING_SHIELD)))
					} else AdsArmorValue.NONE
					//Compute contributions from armor.
					for(equip <- equipset) {
						val equipped = victim.equipment(equip)
						val totalArmorValue = AdsArmorValue(damage, AdsArmorValue.getFrom(equipped, equip))
						val resultDmg = (totalArmorValue + totalShieldValue).apply(baseDmg)
						avgdamage += resultDmg
						val armorDamage = baseDmg-totalArmorValue.apply(baseDmg)-totalArmorValue.hard
						val degrade = damage.onDamageArmor(resultDmg, armorDamage, equipped)
						if(degrade > 0) equipped.consume(Math.ceil(degrade/3f).toInt)
					}
					avgdamage /= equipset.size
					//Miscellaneous resistances.
					var dmgmult = 1f
					if(damage.isInstanceOf[TDmgTypeNatural]) dmgmult *= 1f-Math.min(1f, victim.getStatus(Statuses.resistance)/5f)
					// TODO: Innate resistances.
					finaldamage += damage.onDamageEntity(avgdamage*dmgmult, avgdamage*(1-dmgmult), victim)
				}
				if(finaldamage <= 0) event.setCanceled(true)
				else event.setAmount(finaldamage)
			}
		case other =>
	}
}
