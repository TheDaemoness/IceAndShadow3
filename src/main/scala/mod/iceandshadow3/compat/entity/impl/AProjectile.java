package mod.iceandshadow3.compat.entity.impl;

import mod.iceandshadow3.basics.BLogicProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class AProjectile extends BAEntity<BLogicProjectile> implements IProjectile {

	protected AProjectile(BLogicProjectile blp, EntityType<? extends AProjectile> mctype, World world) {
		super(blp, mctype, world);
	}

	@Override
	public void shoot(double x, double y, double z, float vel, float deviation) {

	}

	@Override
	public boolean shouldRiderSit() {
		return false;
	}

	@Override
	public boolean canRiderInteract() {
		return false;
	}

	@Override
	public boolean canBeRiddenInWater(Entity rider) {
		return false;
	}
}