package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BLogicEntityMob;
import mod.iceandshadow3.lib.base.LogicPair;
import mod.iceandshadow3.lib.base.LogicProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AMob extends MobEntity implements LogicProvider.Mob {
	private final BLogicEntityMob logic;
	private int variant;
	AMob(BLogicEntityMob mobMentality, EntityType<? extends AMob> mctype, World world) {
		super(mctype, world);
		logic = mobMentality;
		variant = 0;
		//TODO: Determine variant pre-spawn.
	}

	@Nullable
	@Override
	public LogicPair<BLogicEntityMob> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}
}
