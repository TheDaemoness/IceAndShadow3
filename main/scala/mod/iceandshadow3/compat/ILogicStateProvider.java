package mod.iceandshadow3.compat;

import javax.annotation.Nullable;

import mod.iceandshadow3.IceAndShadow3;
import mod.iceandshadow3.basics.BStateData;
import mod.iceandshadow3.basics.ILogicProvider;
import mod.iceandshadow3.data.DataTreeMap;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCollection;
import net.minecraft.nbt.NBTTagCompound;

// Maybe reconsider writing the adapter classes in Scala.
// This is effectively a mixin where everything should be protected.

public interface ILogicStateProvider<LogicType extends BLogic, InstanceType> extends ILogicProvider<LogicType> {
	net.minecraft.nbt.NBTTagCompound getNBT(InstanceType instance);

	@Nullable
	default BStateData getStateData(InstanceType instance){
		final BStateData sd = getLogic().getDefaultStateData(getVariant());
		if(sd != null) {
			final DataTreeMap tree = sd.getDataTree();
			tree.fromNBT(getNBT(instance).getCompound(IceAndShadow3.MODID));
			//TODO: Consider using CNbtTree here.
			sd.fromDataTree(tree);
		}
		return sd;
	}
	default void saveStateData(InstanceType instance, BStateData state) {
		if(state == null || !state.needsWrite()) return;
		getNBT(instance).setTag(IceAndShadow3.MODID, state.getDataTree().toNBT());
	}
}
