package mod.iceandshadow3;

import mod.iceandshadow3.compat.client.AParticleType;
import mod.iceandshadow3.compat.client.BinderParticle$;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
class InitClient {
	static void finishParticles() {
		for(IParticleData apt : BinderParticle$.MODULE$.freeze()) {
			if(apt instanceof AParticleType) ((AParticleType) apt).addFactory();
		}
	}
}
