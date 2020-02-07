package mod.iceandshadow3.lib.compat.client.impl

import mod.iceandshadow3.lib.util.collect.BinderLazy
import mod.iceandshadow3.lib.{ParticleType, BParticleType}
import net.minecraft.particles.{IParticleData, ParticleTypes}

object BinderParticle extends BinderLazy[BParticleType, ParticleType, IParticleData]({
	new AParticleType(_)
}) {
	def populate(): Unit = {
		import mod.iceandshadow3.multiverse.misc.Particles._
		add(smoke_large, ParticleTypes.LARGE_SMOKE)
	}
}
