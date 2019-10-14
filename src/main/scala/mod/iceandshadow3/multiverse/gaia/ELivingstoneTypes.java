package mod.iceandshadow3.multiverse.gaia;

import java.util.Random;

public enum ELivingstoneTypes {
	COMMON("gray"),
	GREEN("green"),
	CYAN("cyan"),
	BLUE("blue"),
	WHITE("white"),
	BROWN("brown"),
	PURPLE("purple"),
	RED("red"),
	GOLD("gold");

	public final String name;
	ELivingstoneTypes(String naem) {
		name = naem;
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
