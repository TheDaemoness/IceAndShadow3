package mod.iceandshadow3.util;

/** Utility functions for using the bits (esp. the sign bit) on an integer for
 * something else.
 */
public class SIntBits {
	public static boolean areAllSet(int value, int bitmask) {
		return (value & bitmask) == bitmask;
	}

	public static boolean areAnySet(int value, int bitmask) {
		return (value & bitmask) != 0;
	}

	public static int encode(int value, boolean flag) {
		return ((value & (-1 >>> 1)) + (flag ? 1 : 0)) * (flag ? -1 : 1);
	}

	// The following are common and simple-enough operations, but they're annoying
	// to type out.

	public static boolean signBit(int value) {
		return value < 0;
	}

	public static int value(int value) {
		return Math.abs(value + (signBit(value) ? 1 : 0));
	}
}
