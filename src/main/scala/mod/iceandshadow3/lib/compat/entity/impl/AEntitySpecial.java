package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BLogicEntitySpecial;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class AEntitySpecial extends BAEntity<BLogicEntitySpecial> {
	protected AEntitySpecial(BLogicEntitySpecial ble, EntityType<? extends AEntitySpecial> mctype, World world) {
		super(ble, mctype, world);
	}
}
