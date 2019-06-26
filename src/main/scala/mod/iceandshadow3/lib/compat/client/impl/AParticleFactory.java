package mod.iceandshadow3.lib.compat.client.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BParticleType;
import mod.iceandshadow3.lib.compat.client.IParticle;
import mod.iceandshadow3.spatial.IVec3;
import mod.iceandshadow3.util.Color;
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
	private final BParticleType type;
	AParticleFactory(BParticleType bpt) {
		this.type = bpt;
	}
	@OnlyIn(Dist.CLIENT)
	class AParticle extends Particle implements IParticle {
		private final BParticleType type;
		private AParticle(
			BParticleType bpt, World worldIn,
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
		public void renderParticle(
			@Nonnull BufferBuilder buffer, @Nonnull ActiveRenderInfo entityIn, float partialTicks,
			float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ
		) {
			//TODO: Implement.
		}

		@Nonnull
		@Override
		public IParticleRenderType func_217558_b() {
			//TODO: The below is a no-op. Return something meaningful.
			return IParticleRenderType.field_217606_f;
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
