package mod.iceandshadow3.lib.spatial

object IVec3 {
	val SUB_BITS: Int = 16
	val SUB_MULT: Long = 1L << SUB_BITS
	val SUB_MASK: Long = SUB_MULT - 1

	def fromDouble(value: Double): Long = {
		val whole: Long = value.toLong
		(whole << SUB_BITS) + ((value - whole) * SUB_MULT).toLong
	}
	def toDouble(value: Long): Double =
		toBlockCoord(value) + toSubCoord(value).toDouble / SUB_MULT

	def fromBlockCoord(value: Int): Long = value << SUB_BITS
	def toBlockCoord(value: Long): Int = (value >> SUB_BITS).toInt
	def toSubCoord(value: Long): Int = (value & SUB_MASK).toInt
}

import mod.iceandshadow3.lib.spatial.IVec3._

/** An immutable fixed-precision 3d vector class.
*/
trait IVec3
	extends Comparable[IVec3]
	with IPositionalFine
	with IPosBlock
{
	def xRaw: Long
	def yRaw: Int
	def zRaw: Long

	def xBlock: Int = toBlockCoord(xRaw)
	def yBlock: Int = toBlockCoord(yRaw)
	def zBlock: Int = toBlockCoord(zRaw)
	def xSubBlock: Int = toSubCoord(xRaw)
	def ySubBlock: Int = toSubCoord(yRaw)
	def zSubBlock: Int = toSubCoord(zRaw)
	def xDouble: Double = toDouble(xRaw)
	def yDouble: Double = toDouble(yRaw)
	def zDouble: Double = toDouble(zRaw)

	/** Returns a mutable copy of this vector.
		*/
	def copy: Vec3Mutable = new Vec3Mutable(this.xRaw, this.yRaw, this.zRaw)
	def asMutable: Vec3Mutable = copy
	def asFixed: Vec3Fixed = new Vec3Fixed(this.xRaw, this.yRaw, this.zRaw)

	/** Returns the magnitude as a raw Long that needs to be square rooted.
		*/
	def magRaw: Long = {
		val x = xRaw; val y = yRaw; val z = zRaw
		x * x + y * y + z * z
	}
	def mag: Double = Math.sqrt(toDouble(magRaw))
	/** Returns the horizontal component of the magnitude as a raw Long that needs to be square rooted.
		*/
	def magRawH: Long = {
		val x = xRaw; val z = zRaw
		x * x + z * z
	}
	def magH: Double = Math.sqrt(toDouble(magRawH))

	def dot(b: IVec3): Double = {
		val x = xRaw; val y = yRaw; val z = zRaw
		toDouble(x * b.xRaw + y.toLong * b.yRaw + z * b.zRaw)
	}
	def delta(b: IVec3): IVec3 = {
		val x = xRaw; val y = yRaw; val z = zRaw
		new Vec3Mutable(b.xRaw - x, b.yRaw - y, b.zRaw - z)
	}
	def angle(b: IVec3): Double = {
		val result = Math.acos(this.dot(b) / (mag * b.mag))
		if (result > Math.PI) result - 2 * Math.PI
		else result
	}

	override def equals(o: Any): Boolean = o match {
		case b: IVec3 =>
			val x = xRaw; val y = yRaw; val z = zRaw
			y == b.yRaw && x == b.xRaw && z == b.zRaw
		case _ => false
	}

	override def toString: String = s"<$xDouble,$yDouble,$zDouble>"
	override def compareTo(b: IVec3): Int = java.lang.Long.compare(magRaw, b.magRaw)
	override def posFine = this
}

