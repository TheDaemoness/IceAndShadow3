package mod.iceandshadow3.lib.forge

import mod.iceandshadow3.damage.{AdsArmorValue, TDmgTypeNatural}
import mod.iceandshadow3.lib.compat.entity.CNVEntity
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
			val mult = attack.baseDamage / event.getAmount
			if(mult <= 0f) event.setCanceled(true)
			else {
				val equipset = CollectUtils.randomPick(attack.form.relevantEquips.toIndexedSeq, victim.rng())
				var finaldamage = 0f
				for(damage <- attack.instances) {
					var avgdamage = 0f
					for(equip <- equipset) {
						var sumSoft = 0f
						var sumHard = 0f
						val equipped = victim.equipment(equip)
						for(resist <- AdsArmorValue.getFrom(equipped, equip)) {
							if(resist._1.isInstance(damage)) {
								sumHard += resist._2.hard
								sumSoft += resist._2.soft
							}
						}
						val baseDmg = damage.baseDamage*mult
						val resultDmg = AdsArmorValue(sumHard, sumSoft).apply(baseDmg)
						avgdamage += resultDmg
						val degrade = damage.onDamageArmor(resultDmg, baseDmg-resultDmg, equipped)
						if(degrade >= 1) equipped.consume(degrade.toInt)
					}
					avgdamage /= equipset.size
					var dmgmult = 1f;
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
