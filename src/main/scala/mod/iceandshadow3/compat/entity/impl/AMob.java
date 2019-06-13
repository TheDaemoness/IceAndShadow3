package mod.iceandshadow3.compat.entity.impl;

import mod.iceandshadow3.basics.BLogicMob;
import mod.iceandshadow3.basics.util.LogicPair;
import mod.iceandshadow3.compat.ILogicMobProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AMob extends MobEntity implements ILogicMobProvider {
	private final BLogicMob logic;
	private int variant;
	AMob(BLogicMob mobMentality, EntityType<? extends AMob> mctype, World world) {
		super(mctype, world);
		logic = mobMentality;
		variant = 0;
		//TODO: Determine variant pre-spawn.
	}

	@Nullable
	@Override
	public LogicPair<BLogicMob> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}
}
