package mod.iceandshadow3.lib.compat.client.impl;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.ParticleType;
import mod.iceandshadow3.lib.compat.client.IParticle;
import mod.iceandshadow3.lib.spatial.IVec3;
import mod.iceandshadow3.lib.util.Color;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class AParticleFactory implements IParticleFactory<BasicParticleType> {
	private final ParticleType type;
	AParticleFactory(ParticleType bpt) {
		this.type = bpt;
	}
	@OnlyIn(Dist.CLIENT)
	class AParticle extends Particle implements IParticle {
		private final ParticleType type;
		private AParticle(
				ParticleType bpt, World worldIn,
				double posXIn, double posYIn, double posZIn,
				double xSpeed, double ySpeed, double zSpeed
		) {
			super(worldIn, posXIn, posYIn, posZIn, xSpeed, ySpeed, zSpeed);
			type = bpt;
			type.init(this, rand);
			this.setMaxAge(type.maxTicks());
		}

		@Override
		public void tick() {
			type.tick(this, this.rand);
			super.tick();
		}

		@Override
		public void func_225606_a_(IVertexBuilder scene, ActiveRenderInfo entityIn, float partialTicks) {
			//TODO: Implement.
		}

		@Nonnull
		@Override
		public IParticleRenderType getRenderType() {
			//TODO: The below is a lie. Return something useful later.
			return IParticleRenderType.NO_RENDER;
		}

		@Override
		public void iasSetColor(Color color) {
			this.setColor(color.red(), color.green(), color.blue());
		}

		@Override
		public void iasSetAlpha(float alpha) {
			this.setAlphaF(alpha);
		}

		@Override
		public void iasSetScale(float scale) { this.setSize(scale, scale); }

		@Override
		public void iasSetGravity(float gravity) {
			this.particleGravity = gravity;
		}

		@Override
		public void iasSetMotion(IVec3 what) {
			this.motionX = what.xDouble();
			this.motionY = what.yDouble();
			this.motionZ = what.zDouble();
		}

		@Override
		public void iasSetTexture(int mcid) {
			//TODO: Look into custom textures for IaS3 particles.
		}

		@Override
		public int age() {
			return this.age;
		}
	}

	@Override
	public Particle makeParticle(
		@Nonnull BasicParticleType typeIn, @Nonnull World worldIn,
		double x, double y, double z,
		double xSpeed, double ySpeed, double zSpeed
	) {
		//TODO: typeIn parameters?
		if(!IaS3.getCfgClient().low_particles.isTrue() || type.isInfo()) {
			return this.new AParticle(type, worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
		} else return null;
	}
}
