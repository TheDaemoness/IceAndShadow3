package mod.iceandshadow3.compat.entity.impl;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicCommonEntity;
import mod.iceandshadow3.basics.BStateData;
import mod.iceandshadow3.basics.util.LogicPair;
import mod.iceandshadow3.basics.util.ILogicEntityProvider;
import mod.iceandshadow3.data.DataTreeMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BAEntity<Logic extends BLogicCommonEntity> extends Entity implements ILogicEntityProvider {
	protected final Logic logic;
	protected BStateData state;
	protected int variant;
	BAEntity(Logic l, EntityType<? extends BAEntity<Logic>> mctype, World world) {
		super(mctype, world);
		logic = l;
		variant = 0;
		//TODO: Determine variant pre-spawn.
		state = logic.getDefaultStateData(variant);
	}


	@Override
	protected void registerData() {

	}

	@Override
	protected void readAdditional(CompoundNBT nbt) {
		final DataTreeMap dtm = state.exposeDataTree();
		try {
			dtm.fromNBT(nbt.getCompound(IaS3.MODID));
			state.fromDataTree(dtm);
		} catch (ClassCastException e) {
			IaS3.logger().error("NBT type mismatch when loading "+this+": "+e.getMessage());
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		nbt.put(IaS3.MODID, state.exposeDataTree().toNBT());
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return new SSpawnObjectPacket(this);
	}

	@Nullable
	@Override
	public LogicPair<BLogicCommonEntity> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}
}
