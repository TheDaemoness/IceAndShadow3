package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.item.WUsageItem
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainGaia
import mod.iceandshadow3.multiverse.misc.StatusEffects

object LIJade {
	val variants = 0 to 4
}
class LIJade(val size: Int) extends BLogicItem(DomainGaia, "jade_"+size) {
	assert(size < 5)
	override def stackLimit = 32 >> size

	override def tier = size+1

	private val ticks = List(120, 150, 250, 470, 940)
	override def onUseGeneral(context: WUsageItem) = {
		val user = context.stack.owner
		if(user.hp < user.hpMax && user(StatusEffects.renewal).getAmp <= size) {
			user.playSound(WSound("minecraft:entity.generic.eat"), 0.5f)
			StatusEffects.renewal.forTicks(ticks(size), size+1)(user)
			context.stack.degrade()
			E3vl.TRUE
		} else E3vl.FALSE
	}
}