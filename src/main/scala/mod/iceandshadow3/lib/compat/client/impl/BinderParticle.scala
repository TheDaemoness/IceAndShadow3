package mod.iceandshadow3.lib.compat.client.impl

import mod.iceandshadow3.lib.util.collect.BinderLazy
import mod.iceandshadow3.lib.{BParticleType, ParticleType}
import net.minecraft.particles.{IParticleData, ParticleTypes}

object BinderParticle extends BinderLazy[ParticleType, BParticleType, IParticleData]({
	new AParticleType(_)
}) {
	def populate(): Unit = {
		import mod.iceandshadow3.multiverse.misc.Particles._
		add(smoke_large, ParticleTypes.LARGE_SMOKE)
	}
}
