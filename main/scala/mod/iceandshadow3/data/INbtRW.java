package mod.iceandshadow3.data;

import net.minecraft.nbt.INBTBase;

public interface INbtRW {
	INBTBase toNBT();

	boolean fromNBT(INBTBase tag) throws ClassCastException, IllegalArgumentException;
}
