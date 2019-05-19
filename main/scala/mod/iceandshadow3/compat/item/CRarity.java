package mod.iceandshadow3.compat.item;

import net.minecraft.item.EnumRarity;

public enum CRarity implements Comparable<CRarity> {
	//WARNING: Order-sensitive.
	COMMON(EnumRarity.COMMON),
	UNCOMMON(EnumRarity.UNCOMMON),
	RARE(EnumRarity.RARE),
	EPIC(EnumRarity.EPIC);
	
	final EnumRarity rarity;

	static CRarity get(EnumRarity r) {
		return CRarity.values()[r.ordinal()];
	}
	CRarity(EnumRarity r) {
		rarity = r;
	}
}
