package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BLogicEntitySpecial;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class AEntity extends BAEntity<BLogicEntitySpecial> {
	protected AEntity(BLogicEntitySpecial ble, EntityType<? extends AEntity> mctype, World world) {
		super(ble, mctype, world);
	}
}
