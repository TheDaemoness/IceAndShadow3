package mod.iceandshadow3.lib.compat.util

import java.util.Random

object SRandom extends Random {
	def getRNG(entity: net.minecraft.entity.LivingEntity): Random =
		if(entity == null) this else entity.getRNG
}
