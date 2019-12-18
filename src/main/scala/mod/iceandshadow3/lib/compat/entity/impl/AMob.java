package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BLogicEntityMob;
import mod.iceandshadow3.lib.base.LogicProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AMob extends MobEntity implements LogicProvider.Mob {
	private final BLogicEntityMob logic;
	AMob(BLogicEntityMob mobMentality, EntityType<? extends AMob> mctype, World world) {
		super(mctype, world);
		logic = mobMentality;
		//TODO: Determine variant pre-spawn.
	}

	@Nullable
	@Override
	public BLogicEntityMob getLogic() {
		return logic;
	}
}
