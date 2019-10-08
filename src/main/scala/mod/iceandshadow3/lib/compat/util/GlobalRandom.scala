package mod.iceandshadow3.lib.compat.util

import java.util.Random

import javax.annotation.Nullable

object GlobalRandom extends Random {
	def getRNG(@Nullable entity: net.minecraft.entity.LivingEntity): Random =
		if(entity == null) this else entity.getRNG
}
