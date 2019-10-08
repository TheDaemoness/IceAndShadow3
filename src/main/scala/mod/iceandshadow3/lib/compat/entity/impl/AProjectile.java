package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BLogicEntityProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class AProjectile extends BAEntity<BLogicEntityProjectile> implements IProjectile {

	protected AProjectile(BLogicEntityProjectile blp, EntityType<? extends AProjectile> mctype, World world) {
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
	public boolean canBeRiddenInWater(net.minecraft.entity.Entity rider) {
		return false;
	}
}
