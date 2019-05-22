package mod.iceandshadow3.compat

import java.util.Random

object SRandom extends Random {
	def getRNG(entity: net.minecraft.entity.EntityLivingBase): Random =
		if(entity == null) this else entity.getRNG
}