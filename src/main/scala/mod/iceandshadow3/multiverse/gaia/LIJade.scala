package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.item.WUsageItem
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.misc.StatusEffects

object LIJade {
	val variants = 0 to 4
	val ticks = List(120, 150, 250, 470, 940)
}
class LIJade(val size: Int) extends LogicItemMulti(DomainGaia, "jade_"+size, size+1, 32 >> size) {
	override def onUseGeneral(context: WUsageItem) = {
		val user = context.stack.owner
		if(user.hp < user.hpMax && user(StatusEffects.renewal).getAmp <= size) {
			user.playSound(WSound("minecraft:entity.generic.eat"), 0.5f)
			StatusEffects.renewal.forTicks(LIJade.ticks(size), size+1)(user)
			context.stack.degrade()
			E3vl.TRUE
		} else E3vl.FALSE
	}
}