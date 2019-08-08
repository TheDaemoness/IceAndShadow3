package mod.iceandshadow3.lib.util;

/** Utility functions for using the bits (esp. the sign bit) on a long integer for
 * something else.
 */
public class IntBitUtils {
	public static boolean areAllSet(long value, long bitmask) {
		return (value & bitmask) == bitmask;
	}

	public static boolean areAnySet(long value, long bitmask) {
		return (value & bitmask) != 0;
	}

	public static long encode(long value, boolean flag) {
		return ((value & (-1 >>> 1)) + (flag ? 1 : 0)) * (flag ? -1 : 1);
	}

	public static boolean signBit(long value) {
		return value < 0;
	}

	public static long value(long value) {
		return Math.abs(value + (signBit(value) ? 1 : 0));
	}

	private static long spreadBits(int value) {
		//Ripped from https://lemire.me/blog/2018/01/08/how-fast-can-you-bit-interleave-32-bit-integers/
		long retval = Integer.toUnsignedLong(value);
		retval = (retval^(retval << 16)) & 0x0000ffff0000ffffL;
		retval = (retval^(retval <<  8)) & 0x00ff00ff00ff00ffL;
		retval = (retval^(retval <<  4)) & 0x0f0f0f0f0f0f0f0fL;
		retval = (retval^(retval <<  2)) & 0x3333333333333333L;
		retval = (retval^(retval <<  1)) & 0x5555555555555555L;
		return retval;
	}

	public static long mixIntBits(int higher, int lower) {
		return (spreadBits(higher) << 1) | spreadBits(lower);
	}
}
