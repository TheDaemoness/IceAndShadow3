package mod.iceandshadow3.multiverse.gaia;

import java.util.Random;

public enum ELivingstoneTypes {
	COMMON("gray", 1),
	GREEN("green", 2),
	CYAN("cyan", 2),
	BLUE("blue", 2),
	WHITE("white", 2),
	BROWN("brown", 3),
	PURPLE("purple", 4),
	RED("red", 5),
	GOLD("gold", 5);

	public final int rarity;
	public final String name;
	ELivingstoneTypes(String name, int rarity) {
		this.name = name;
		this.rarity = rarity;
	}

	public String appendTo(String in) {
		return in + '_' + name;
	}

	public static ELivingstoneTypes getCommon(Random r) {
		final ELivingstoneTypes res = common[r.nextInt(common.length)];
		if(res == null) return COMMON;
		else return res;
	}
	public static ELivingstoneTypes getAny(Random r) {
		final ELivingstoneTypes res = common[r.nextInt(common.length)];
		if(res == null) return rare[r.nextInt(rare.length)];
		else return res;
	}

	private static final ELivingstoneTypes[] common = {COMMON, COMMON, CYAN, BLUE, GREEN, WHITE, null};
	private static final ELivingstoneTypes[] rare = {BROWN, BROWN, PURPLE, PURPLE, RED};
	private static final ELivingstoneTypes[] synth = {RED, GOLD}; //To use later.
}
