package mod.iceandshadow3.compat;

import javax.annotation.Nullable;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BStateData;
import mod.iceandshadow3.basics.ILogicProvider;
import mod.iceandshadow3.data.DataTreeMap;

// Maybe reconsider writing the adapter classes in Scala.
// This is effectively a mixin where everything should be protected.

public interface ILogicStateProvider<LogicType extends BLogic, InstanceType> extends ILogicProvider<LogicType> {
	net.minecraft.nbt.NBTTagCompound getNBT(InstanceType instance);

	@Nullable
	default BStateData getStateData(InstanceType instance){
		final BStateData sd = getLogic().getDefaultStateData(getVariant());
		if(sd != null) {
			final DataTreeMap tree = sd.newDataTree();
			tree.fromNBT(getNBT(instance).getCompound(IaS3.MODID));
			//TODO: Consider using CNbtTree here.
			sd.fromDataTree(tree);
		}
		return sd;
	}
	default void saveStateData(InstanceType instance, BStateData state) {
		if(state == null || !state.needsWrite()) return;
		getNBT(instance).setTag(IaS3.MODID, state.newDataTree().toNBT());
	}
}
