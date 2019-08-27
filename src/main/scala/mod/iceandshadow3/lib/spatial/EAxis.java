package mod.iceandshadow3.lib.spatial;

public enum EAxis {
	Y("y"),
	X("x"),
	Z("z");

	private final String name;
	EAxis(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
