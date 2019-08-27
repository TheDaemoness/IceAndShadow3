package mod.iceandshadow3.lib.spatial;

public enum EAxis {
	DOWN_UP("y"),
	WEST_EAST("x"),
	NORTH_SOUTH("z");

	private final String name;
	EAxis(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
