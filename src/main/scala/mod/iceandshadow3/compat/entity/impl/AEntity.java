package mod.iceandshadow3.compat.entity.impl;

import mod.iceandshadow3.basics.BLogicEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class AEntity extends BAEntity<BLogicEntity> {
	protected AEntity(BLogicEntity ble, EntityType<? extends AEntity> mctype, World world) {
		super(ble, mctype, world);
	}
}
