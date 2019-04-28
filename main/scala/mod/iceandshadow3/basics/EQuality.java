package mod.iceandshadow3.basics;

import mod.iceandshadow3.compat.CRarity;

public enum EQuality {
	NATURAL,
	NATURAL_UNCOMMON,
	NATURAL_RARE(CRarity.UNCOMMON, EDeathPolicy.LOSE_LOW),
	NATURAL_EXOTIC(CRarity.RARE, EDeathPolicy.LOSE_LOW),
	ANCIENT(CRarity.UNCOMMON, EDeathPolicy.LOSE);
	
	public final CRarity rarity;
	public final EDeathPolicy ondeath, ondeath_brutal;
	
	private EQuality(CRarity r, EDeathPolicy dp, EDeathPolicy dp_b) {
		rarity = r;
		ondeath = dp;
		ondeath_brutal = dp_b;
	}
	private EQuality(CRarity r, EDeathPolicy dp) {
		this(r, dp, dp.weaken());
	}
	private EQuality(CRarity r) {
		this(r, EDeathPolicy.DEFAULT);
	}
	private EQuality() {
		this(CRarity.COMMON);
	}
}
