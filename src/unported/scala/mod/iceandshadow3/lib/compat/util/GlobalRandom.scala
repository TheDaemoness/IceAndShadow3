package mod.iceandshadow3.lib.compat.util

import java.util.Random

import javax.annotation.Nullable
import net.minecraft.entity.LivingEntity

object GlobalRandom extends Random {
	def getRNG(@Nullable entity: net.minecraft.entity.Entity): Random = entity match {
		case el: LivingEntity => el.getRNG
		case _ => this
	}
}
