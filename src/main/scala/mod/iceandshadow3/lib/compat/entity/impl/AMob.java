package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.LogicEntityMob;
import mod.iceandshadow3.lib.base.ProviderLogic;
import mod.iceandshadow3.lib.compat.id.WId;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AMob extends MobEntity implements ProviderLogic.Mob {
	private final LogicEntityMob logic;
	AMob(LogicEntityMob mobMentality, EntityType<? extends AMob> mctype, World world) {
		super(mctype, world);
		logic = mobMentality;
		//TODO: Determine variant pre-spawn.
	}

	@Nullable
	@Override
	public LogicEntityMob getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}
