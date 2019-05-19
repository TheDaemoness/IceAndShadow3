package mod.iceandshadow3.util;

public enum L3 {
	FALSE(-1), NULL(0), TRUE(1);

	private final int value;

	L3(int value) {
		this.value = value;
	}

	public L3 not() {
		switch (this) {
			case TRUE:
				return FALSE;
			case FALSE:
				return TRUE;
			default:
				return NULL;
		}
	}

	public L3 and(L3 b) {
		if (this == FALSE || b == FALSE)
			return FALSE;
		if (this == NULL || b == NULL)
			return NULL;
		return TRUE;
	}

	public L3 or(L3 b) {
		if (this == TRUE || b == TRUE)
			return TRUE;
		if (this == NULL || b == NULL)
			return NULL;
		return FALSE;
	}

	public static L3 fromBool(boolean value) {
		return value?TRUE:FALSE;
	}
	public static L3 fromInt(int value) {
		if (value > 0)
			return TRUE;
		if (value < 0)
			return FALSE;
		return NULL;
	}
}
