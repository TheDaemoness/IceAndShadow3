package mod.iceandshadow3.lib.util

/** Utility functions for using the bits (esp. the sign bit) on a long integer for something else.
	*/
object IntBitUtils {
	def areAllSet(value: Long, bitmask: Long) = (value & bitmask) == bitmask

	def areAnySet(value: Long, bitmask: Long) = (value & bitmask) != 0

	def signBit(value: Long) = value < 0

	def value(value: Long) = Math.abs(value + (if (signBit(value)) 1
	else 0))

	private def spreadBits(value: Int) = {
		//Ripped from https://lemire.me/blog/2018/01/08/how-fast-can-you-bit-interleave-32-bit-integers/
		var retval = Integer.toUnsignedLong(value)
		retval = (retval ^ (retval << 16)) & 0x0000ffff0000ffffL
		retval = (retval ^ (retval << 8)) & 0x00ff00ff00ff00ffL
		retval = (retval ^ (retval << 4)) & 0x0f0f0f0f0f0f0f0fL
		retval = (retval ^ (retval << 2)) & 0x3333333333333333L
		retval = (retval ^ (retval << 1)) & 0x5555555555555555L
		retval
	}

	def mixIntBits(higher: Int, lower: Int) = (spreadBits(higher) << 1) | spreadBits(lower)
}