package mod.iceandshadow3.lib.compat.entity.impl;

import mod.iceandshadow3.lib.BDomain;
import mod.iceandshadow3.lib.BLogicEntity;
import mod.iceandshadow3.lib.base.LogicProvider;
import mod.iceandshadow3.lib.compat.WId;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;
import scala.Option;
import scala.reflect.ClassTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BAEntity<Logic extends BLogicEntity> extends Entity implements LogicProvider.Entity {
	protected final Logic logic;
	BAEntity(Logic l, EntityType<? extends BAEntity<Logic>> mctype, World world) {
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
	public BLogicEntity getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}
