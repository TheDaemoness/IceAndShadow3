package mod.iceandshadow3.compat.client.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BParticleType;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;

public class AParticleType extends BasicParticleType {
	AParticleType(BParticleType pt) {
		super(pt.isInfo() || pt.hiVis());
		this.setRegistryName(new ResourceLocation(IaS3.MODID, pt.name()));
	}
}
