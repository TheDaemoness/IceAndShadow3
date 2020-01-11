package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.LogicEntity;
import mod.iceandshadow3.lib.base.ProviderLogic;
import mod.iceandshadow3.lib.compat.id.WId;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AEntity<Logic extends LogicEntity> extends Entity implements ProviderLogic.Entity {
	protected final Logic logic;
	AEntity(Logic l, EntityType<? extends AEntity<Logic>> mctype, World world) {
		super(mctype, world);
		logic = l;
		//TODO: Determine variant pre-spawn.
	}

	@Override
	protected void registerData() {

	}

	@Override
	protected void readAdditional(CompoundNBT nbt) {
		//TODO: Reflective BVar collection?
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		//TODO: Reflective BVar collection?
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return new SSpawnObjectPacket(this);
	}

	@Nullable
	@Override
	public LogicEntity getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}
