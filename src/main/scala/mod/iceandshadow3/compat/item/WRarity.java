package mod.iceandshadow3.compat.item;

import net.minecraft.item.EnumRarity;

public enum WRarity implements Comparable<WRarity> {
	//WARNING: Order-sensitive.
	COMMON(EnumRarity.COMMON),
	UNCOMMON(EnumRarity.UNCOMMON),
	RARE(EnumRarity.RARE),
	EPIC(EnumRarity.EPIC);
	
	final EnumRarity rarity;

	static WRarity get(EnumRarity r) {
		return WRarity.values()[r.ordinal()];
	}
	WRarity(EnumRarity r) {
		rarity = r;
	}
}
