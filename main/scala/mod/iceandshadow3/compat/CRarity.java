package mod.iceandshadow3.compat;

import net.minecraft.item.EnumRarity;

public enum CRarity implements Comparable<CRarity> {
	COMMON(EnumRarity.COMMON),
	UNCOMMON(EnumRarity.UNCOMMON),
	RARE(EnumRarity.RARE),
	EPIC(EnumRarity.EPIC);
	
	final EnumRarity rarity;
	
	//WARNING: Order-sensitive.
	static CRarity get(EnumRarity r) {
		return CRarity.values()[r.ordinal()];
	}
	private CRarity(EnumRarity r) {
		rarity = r;
	}
}
