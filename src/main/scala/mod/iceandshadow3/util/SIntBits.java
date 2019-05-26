package mod.iceandshadow3.util;

/** Utility functions for using the bits (esp. the sign bit) on a long integer for
 * something else.
 */
public class SIntBits {
	public static boolean areAllSet(long value, long bitmask) {
		return (value & bitmask) == bitmask;
	}

	public static boolean areAnySet(long value, long bitmask) {
		return (value & bitmask) != 0;
	}

	public static long encode(long value, boolean flag) {
		return ((value & (-1 >>> 1)) + (flag ? 1 : 0)) * (flag ? -1 : 1);
	}

	// The following are common and simple-enough operations, but are unintuitive or annoying.

	public static boolean signBit(long value) {
		return value < 0;
	}

	public static long value(long value) {
		return Math.abs(value + (signBit(value) ? 1 : 0));
	}
}
