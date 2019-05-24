package mod.iceandshadow3.util;

import java.util.function.Consumer;

public enum L3 {
	TRUE(true) {
		@Override
		public L3 not() {return FALSE;}
	},
	NEUTRAL(null) {
		@Override
		public L3 not() {return NEUTRAL;}
		@Override
		public L3 unlessTrue(boolean which) {
			return which?TRUE:this;
		}
		@Override
		public void forBoolean(Consumer<Boolean> what) {} //No-op.
		@Override
		public L3 unlessFalse(boolean which) {
			return which?this:FALSE;
		}
	},
	FALSE(false) {
		@Override
		public L3 not() {return TRUE;}
	};

	public final Boolean value;
	L3(Boolean v) {
		value = v;
	}

	public void forBoolean(Consumer<Boolean> what) {
		what.accept(value);
	}

	public abstract L3 not();

	/** Returns NEUTRAL (unless this is NEUTRAL, then returns TRUE) if true.
	 * Best used for boolean conversions.
	 */
	public L3 unlessTrue(boolean which) {
		return which?NEUTRAL:this;
	}
	/** Returns NEUTRAL (unless this is NEUTRAL, then returns FALSE) if false.
	 * Best used for boolean conversions.
	 */
	public L3 unlessFalse(boolean which) {
		return which?this:NEUTRAL;
	}

	public L3 and(L3 b) {
		if (this == FALSE || b == FALSE)
			return FALSE;
		if (this == NEUTRAL || b == NEUTRAL)
			return NEUTRAL;
		return TRUE;
	}

	public L3 or(L3 b) {
		if (this == TRUE || b == TRUE)
			return TRUE;
		if (this == NEUTRAL || b == NEUTRAL)
			return NEUTRAL;
		return FALSE;
	}

	public static L3 fromBool(boolean value) {
		return value?TRUE:FALSE;
	}
	public static L3 fromBoolBoxed(Boolean value) {
		return value==null?NEUTRAL:fromBool(value);
	}
	public static L3 fromInt(int value) {
		if (value > 0)
			return TRUE;
		if (value < 0)
			return FALSE;
		return NEUTRAL;
	}
}
