package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.item.WUsageItem
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.misc.StatusEffects

class LIJade extends BLogicItem(DomainGaia, "jade") {
	override def stackLimit(variant: Int) = 32 >> variant
	override def countVariants = 5
	override def getTier(variant: Int) = variant+1
	override protected def getVariantName(variant: Int) = variant.toString

	private val ticks = List(120, 150, 250, 470, 940)
	override def onUseGeneral(variant: Int, context: WUsageItem) = {
		val user = context.user
		if(user.hp < user.hpMax && user(StatusEffects.renewal).getAmp <= variant) {
			user.playSound(WSound("minecraft:entity.generic.eat"), 0.5f)
			StatusEffects.renewal.forTicks(ticks(variant), variant+1)(user)
			context.stack.consume()
			E3vl.TRUE
		} else E3vl.FALSE
	}
}