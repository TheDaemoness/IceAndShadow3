package mod.iceandshadow3.lib.compat.recipe;

public enum ERecipeSize {
	ONE_X_TWO(1,2),
	ONE_X_THREE(1,3),
	TWO_X_ONE(2,1),
	TWO_X_TWO(2,2),
	TWO_X_THREE(2,3),
	THREE_X_ONE(3,1),
	THREE_X_TWO(3,2),
	THREE_X_THREE(3,3);

	public final int width;
	public final int height;
	ERecipeSize(int x, int y) {
		width = x;
		height = y;
	}

	public int size() {
		return width*height;
	}
}
