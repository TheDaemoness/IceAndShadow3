package mod.iceandshadow3.damage.impl

import mod.iceandshadow3.damage.{AdsArmorValue, TDmgTypeNatural}
import mod.iceandshadow3.lib.compat.entity.CNVEntity
import mod.iceandshadow3.lib.compat.entity.state.EquipPoint
import mod.iceandshadow3.lib.util.CollectUtils
import mod.iceandshadow3.multiverse.misc.StatusEffects
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
					damage.applyPre(victim, mult)
					val baseDmg = ads.getAmount(idx) * mult
					//Compute contributions from a shield/auras.
					val shield = victim.equipment(EquipPoint.USING_SHIELD)
					val totalShieldValue = if(attack.form.blockable) {
						AdsArmorValue(damage, AdsArmorValue.getFromShield(shield))
					} else AdsArmorValue.NONE
					val resultDmg = if(equipset.nonEmpty) {
						//Compute contributions from armor.
						var shieldSoakedDamage = 0f
						var avgDamage = 0f
						for (equip <- equipset) {
							val equipped = victim.equipment(equip)
							if (!equipped.isEmpty) {
								val totalArmorValue = AdsArmorValue(damage, AdsArmorValue.getFrom(equipped, equip))
								val reducedDmg = (totalArmorValue + totalShieldValue).apply(baseDmg)
								avgDamage += reducedDmg
								val degrade = damage.onDamageArmor(reducedDmg, totalArmorValue.soaked(baseDmg), equipped)
								if (degrade > 0) equipped.degrade(Math.ceil(degrade / 3f).toInt)
							} else avgDamage += baseDmg
						}
						avgDamage / equipset.size
					} else baseDmg
					if(!shield.isEmpty) {
						val degrade = damage.onDamageArmor(resultDmg, totalShieldValue.soaked(baseDmg), shield)
						if (degrade > 0) shield.degrade(Math.ceil(degrade).toInt)
					}
					//Miscellaneous resistances.
					var dmgmult = 1f
					if(damage.isInstanceOf[TDmgTypeNatural]) {
						dmgmult *= Math.max(0f, 1f - victim(StatusEffects.resistance).getAmp/5f)
					}
					// TODO: Innate resistances.
					finaldamage += damage.onDamageEntity(resultDmg*dmgmult, resultDmg*(1-dmgmult), victim)
					if(dmgmult > 0f) damage.applyPost(victim, dmgmult)
				}
				if(finaldamage <= 0) event.setCanceled(true)
				else event.setAmount(finaldamage)
			}
		case other =>
	}
}
