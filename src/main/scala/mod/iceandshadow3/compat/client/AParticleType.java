package mod.iceandshadow3.compat.client;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraft.util.registry.IRegistry.field_212632_u;

//TODO: Presumably Forge will eventually add a registry for this.

public class AParticleType extends BasicParticleType {
	private final BParticleType pt;

	AParticleType(BParticleType pt) {
		super(new ResourceLocation(IaS3.MODID, pt.name()), pt.isInfo() || pt.hiVis());
		field_212632_u.put(this.getId(), this);
		this.pt = pt;
	}

	@OnlyIn(Dist.CLIENT)
	public void addFactory() {
		Minecraft.getInstance().particles.registerFactory(this, new AParticleFactory(pt));
	}
}
