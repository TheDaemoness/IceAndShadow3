package mod.iceandshadow3.lib.compat.client.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.ParticleType;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;

public class AParticleType extends BasicParticleType {
	AParticleType(ParticleType pt) {
		super(pt.isInfo() || pt.hiVis());
		this.setRegistryName(new ResourceLocation(IaS3.MODID, pt.name()));
	}
}
