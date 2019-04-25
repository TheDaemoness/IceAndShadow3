package mod.iceandshadow3.basics;

import mod.iceandshadow3.Config;
import mod.iceandshadow3.compat.CRarity;

public enum EQuality {
	NATURAL,
	NATURAL_UNCOMMON,
	NATURAL_RARE(CRarity.UNCOMMON, EDeathPolicy.LOSE_LOW),
	NATURAL_EXOTIC(CRarity.EPIC, EDeathPolicy.LOSE_LOW),
	ANCIENT(CRarity.UNCOMMON, EDeathPolicy.LOSE);
	
	protected final CRarity rarity;
	protected final EDeathPolicy ondeath, ondeath_brutal;
	
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
	
	public CRarity getRarity() {
		return rarity;
	}
	public EDeathPolicy getDeathPolicy() {
		return Config.brutal ? ondeath_brutal : ondeath;
	}
}
