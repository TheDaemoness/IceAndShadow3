package mod.iceandshadow3.lib

import java.util.Random

import mod.iceandshadow3.lib.compat.client.IParticle
import mod.iceandshadow3.lib.compat.client.impl.BinderParticle

//NOTE: Do NOT, NOT, NOT make this purely client-side.
//Contains a bunch of code that is dead server-side, but this is also bound to the associated BasicParticleType.

sealed abstract class ParticleType extends BinderParticle.TKey {}

final class ParticleTypePlaceholder extends ParticleType {}

abstract class BParticleType(val name: String, val hiVis: Boolean, val isInfo: Boolean) extends ParticleType {
	BinderParticle.add(this)
	def maxTicks: Int
	def init(what: IParticle, rng: Random): Unit
	def tick(what: IParticle, rng: Random): Unit
}
