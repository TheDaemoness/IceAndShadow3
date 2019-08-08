package mod.iceandshadow3.lib.data;

import net.minecraft.nbt.INBT;

public interface INbtRW {
	INBT toNBT();

	boolean fromNBT(INBT tag) throws ClassCastException, IllegalArgumentException;
}
