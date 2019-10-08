package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BLogicEntity;
import mod.iceandshadow3.lib.base.ILogicEntityProvider;
import mod.iceandshadow3.lib.base.LogicPair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BAEntity<Logic extends BLogicEntity> extends Entity implements ILogicEntityProvider {
	protected final Logic logic;
	protected int variant;
	BAEntity(Logic l, EntityType<? extends BAEntity<Logic>> mctype, World world) {
		super(mctype, world);
		logic = l;
		variant = 0;
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
	public LogicPair<BLogicEntity> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}
}
