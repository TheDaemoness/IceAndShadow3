package mod.iceandshadow3.data;

import net.minecraft.nbt.INBTBase;

public interface INbtSerializable {
	INBTBase toNBT();
	void fromNBT(INBTBase tag) throws ClassCastException, IllegalArgumentException;
}
