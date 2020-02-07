package mod.iceandshadow3.multiverse.gaia;

public enum ELivingstoneType {
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
	ELivingstoneType(String name, int rarity) {
		this.name = name;
		this.rarity = rarity;
	}
}
