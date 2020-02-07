package mod.iceandshadow3.lib.common.model;

public enum ETextureReusePolicy {
	ALL(true,true),
	ENDS(true,false),
	SIDES(false,true),
	NONE(false,false);

	public final boolean ends;
	public final boolean sides;
	ETextureReusePolicy(boolean ends, boolean sides) {
		this.ends = ends;
		this.sides = sides;
	}
}
