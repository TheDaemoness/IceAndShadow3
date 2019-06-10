package mod.iceandshadow3.compat.client

import mod.iceandshadow3.basics.{BParticleType, ParticleType}
import mod.iceandshadow3.util.BinderLazy
import net.minecraft.init.Particles
import net.minecraft.particles.IParticleData


object BinderParticle extends BinderLazy[ParticleType, BParticleType, IParticleData]({
	new AParticleType(_)
}) {
	def populate(): Unit = {
		import mod.iceandshadow3.world.misc.Particles._
		add(smoke_large, Particles.LARGE_SMOKE)
	}
}
