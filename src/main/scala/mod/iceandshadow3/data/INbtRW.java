package mod.iceandshadow3.data;

import net.minecraft.nbt.INBT;

public interface INbtRW {
	INBT toNBT();

	boolean fromNBT(INBT tag) throws ClassCastException, IllegalArgumentException;
}
