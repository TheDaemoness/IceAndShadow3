package mod.iceandshadow3.compat.item;

import net.minecraft.item.Rarity;

public enum WRarity implements Comparable<WRarity> {
	//WARNING: Order-sensitive.
	COMMON(Rarity.COMMON),
	UNCOMMON(Rarity.UNCOMMON),
	RARE(Rarity.RARE),
	EPIC(Rarity.EPIC);
	
	final Rarity rarity;

	static WRarity get(Rarity r) {
		return WRarity.values()[r.ordinal()];
	}
	WRarity(Rarity r) {
		rarity = r;
	}
}
