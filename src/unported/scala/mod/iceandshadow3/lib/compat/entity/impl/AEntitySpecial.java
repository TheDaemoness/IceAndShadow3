package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.LogicEntitySpecial;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class AEntitySpecial extends AEntity<LogicEntitySpecial> {
	protected AEntitySpecial(LogicEntitySpecial ble, EntityType<? extends AEntitySpecial> mctype, World world) {
		super(ble, mctype, world);
	}
}
