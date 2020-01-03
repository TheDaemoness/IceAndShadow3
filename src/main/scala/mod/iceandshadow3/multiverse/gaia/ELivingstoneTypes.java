package mod.iceandshadow3.multiverse.gaia;

import java.util.Random;

public enum ELivingstoneTypes {
	COMMON("gray", 2),
	GREEN("green", 3),
	CYAN("cyan", 3),
	BLUE("blue", 3),
	WHITE("white", 3),
	BROWN("brown", 4),
	PURPLE("purple", 4),
	RED("red", 5),
	GOLD("gold", 5);

	public final int rarity;
	public final String name;
	ELivingstoneTypes(String name, int rarity) {
		this.name = name;
		this.rarity = rarity;
	}

	public static ELivingstoneTypes getCommon(Random r) {
		final ELivingstoneTypes res = common[r.nextInt(common.length)];
		if(res == null) return COMMON;
		else return res;
	}
	public static ELivingstoneTypes getAny(Random r) {
		final ELivingstoneTypes res = common[r.nextInt(common.length)];
		if(res == null) {
			final ELivingstoneTypes resB = uncommon[r.nextInt(uncommon.length)];
			if(resB == null) return rare[r.nextInt(rare.length)];
			else return resB;
		}
		else return res;
	}

	private static final ELivingstoneTypes[] common = {COMMON, BLUE, CYAN, null};
	private static final ELivingstoneTypes[] uncommon = {GREEN, WHITE, null};
	private static final ELivingstoneTypes[] rare = {PURPLE, PURPLE, BROWN, BROWN, RED};
	private static final ELivingstoneTypes[] synth = {RED, GOLD}; //To use later.
}
