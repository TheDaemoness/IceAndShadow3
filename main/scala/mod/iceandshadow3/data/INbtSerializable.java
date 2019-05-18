package mod.iceandshadow3.data;

import net.minecraft.nbt.INBTBase;

public interface INbtSerializable {
	INBTBase toNBT();

	boolean fromNBT(INBTBase tag) throws ClassCastException, IllegalArgumentException;
}
