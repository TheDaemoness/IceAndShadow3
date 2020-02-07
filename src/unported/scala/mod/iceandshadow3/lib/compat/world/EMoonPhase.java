package mod.iceandshadow3.lib.compat.world;

public enum EMoonPhase {
	FULL(0, 4),
	WANING_GIBBOUS(1, 3),
	WANING_HALF(2, 2),
	WANING_CRESCENT(3, 1),
	NEW(4, 0),
	WAXING_CRESCENT(5, 1),
	WAXING_HALF(6, 2),
	WAXING_GIBBOUS(7, 3);

	public final int id;
	public final int strength;

	EMoonPhase(int id, int strength) {
		this.id = id;
		this.strength = strength;
	}
}
