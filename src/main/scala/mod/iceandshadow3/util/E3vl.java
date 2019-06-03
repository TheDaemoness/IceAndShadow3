package mod.iceandshadow3.util;

import java.util.function.Consumer;

public enum E3vl {
	TRUE(true) {
		@Override
		public E3vl not() {return FALSE;}
	},
	NEUTRAL(null) {
		@Override
		public E3vl not() {return NEUTRAL;}
		@Override
		public E3vl unlessTrue(boolean which) {
			return which?TRUE:this;
		}
		@Override
		public void forBoolean(Consumer<Boolean> what) {} //No-op.
		@Override
		public E3vl unlessFalse(boolean which) {
			return which?this:FALSE;
		}
	},
	FALSE(false) {
		@Override
		public E3vl not() {return TRUE;}
	};

	public final Boolean value;
	E3vl(Boolean v) {
		value = v;
	}

	public void forBoolean(Consumer<Boolean> what) {
		what.accept(value);
	}

	public abstract E3vl not();

	/** Returns NEUTRAL (unless this is NEUTRAL, then returns TRUE) if true.
	 * Best used for boolean conversions.
	 */
	public E3vl unlessTrue(boolean which) {
		return which?NEUTRAL:this;
	}
	/** Returns NEUTRAL (unless this is NEUTRAL, then returns FALSE) if false.
	 * Best used for boolean conversions.
	 */
	public E3vl unlessFalse(boolean which) {
		return which?this:NEUTRAL;
	}

	public E3vl and(E3vl b) {
		if (this == FALSE || b == FALSE)
			return FALSE;
		if (this == NEUTRAL || b == NEUTRAL)
			return NEUTRAL;
		return TRUE;
	}

	public E3vl or(E3vl b) {
		if (this == TRUE || b == TRUE)
			return TRUE;
		if (this == NEUTRAL || b == NEUTRAL)
			return NEUTRAL;
		return FALSE;
	}

	public static E3vl fromBool(boolean value) {
		return value?TRUE:FALSE;
	}
	public static E3vl fromBoolBoxed(Boolean value) {
		return value==null?NEUTRAL:fromBool(value);
	}
	public static E3vl fromInt(int value) {
		if (value > 0)
			return TRUE;
		if (value < 0)
			return FALSE;
		return NEUTRAL;
	}
}
