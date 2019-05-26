package mod.iceandshadow3.util

import mod.iceandshadow3.util.Vec3._

// TODO: All of this needs a redesign.

object Vec3 {
	val SUB_BITS: Int = 16
	protected val CHUNK_BITS: Long = 4
	protected val SUB_MULT: Long = 1L << SUB_BITS
	protected val CHUNK_MULT: Long = 1L << CHUNK_BITS
	protected val SUB_MASK: Long = SUB_MULT - 1
	protected val CHUNK_MASK: Long = CHUNK_MULT - 1

	protected[util] def toLong(value: Double): Long = {
		val whole: Long = value.toLong
		(whole << SUB_BITS) + ((value - whole) * SUB_MULT).toLong
	}

	protected def toBlockCoord(value: Long): Long = value >> SUB_BITS
	protected def toSubCoord(value: Long): Int = (value & SUB_MASK).toInt
	protected def toDouble(value: Long): Double =
		toBlockCoord(value) + toSubCoord(value).toDouble / SUB_MULT

	val ZERO: Vec3 = new Vec3(0, 0, 0)
	// Unit vectors.
	val U: Vec3 = new Vec3(0, 1, 0)
	val D: Vec3 = new Vec3(0, -1, 0)
	val S: Vec3 = new Vec3(0, 0, 1)
	val N: Vec3 = new Vec3(0, 0, -1)
	val E: Vec3 = new Vec3(1, 0, 0)
	val W: Vec3 = new Vec3(-1, 0, 0)
}

/** An immutable fixed-precision 3d vector class.
*/
class Vec3 (
	protected var x: Long,
	protected var y: Int,
	protected var z: Long
) extends Comparable[Vec3] with Cloneable {
	def this(x: Long, y: Int, z: Long, shift: Int) = {
		this(x << shift, y << shift, z << shift)
	}
	def this(x: Int, y:Int, z: Int, xShift: Double, yShift: Double, zShift: Double) = this(
		(x << SUB_BITS) + toLong(xShift),
		(y << SUB_BITS) + toLong(yShift).toInt,
		(z << SUB_BITS) + toLong(zShift))
	def this(x: Double, y: Double, z: Double) = this(toLong(x), toLong(y).toInt, toLong(z))

	//TODO: Tuples could stand to be used a lot more here, depending how well they play with Java.
	def xRaw: Long = x
	def yRaw: Int = y
	def zRaw: Long = z
	def xBlock: Long = toBlockCoord(x)
	def yBlock: Int = toBlockCoord(y).toInt
	def zBlock: Long = toBlockCoord(z)
	def xChunk: Int = (toBlockCoord(x) >> CHUNK_BITS).toInt
	def zChunk: Int = (toBlockCoord(z) >> CHUNK_BITS).toInt
	def xSubChunk: Byte = (toBlockCoord(x) & CHUNK_MASK).toByte
	def zSubChunk: Byte = (toBlockCoord(z) & CHUNK_MASK).toByte
	def xSubBlock: Int = toSubCoord(x)
	def ySubBlock: Int = toSubCoord(y)
	def zSubBlock: Int = toSubCoord(z)
	def xDouble: Double = toDouble(x)
	def yDouble: Double = toDouble(y)
	def zDouble: Double = toDouble(z)

/// Returns a mutable copy of this vector.
	def copy: Vec3M = new Vec3M(this)
	def asMutable: Vec3M = copy
	def asImmutable: Vec3 = this

	def mag: Double = Math.sqrt(toDouble(x * x + y * y + z * z))
	def magH: Double = Math.sqrt(toDouble(x * x + z * z))

	def dot(b: Vec3): Double = toDouble(x * b.x + y.toLong * b.y + z * b.z)
	def delta(b: Vec3): Vec3 = new Vec3(b.xRaw - x, b.yRaw - y, b.zRaw - z)
	def angle(b: Vec3): Double = {
		val result = Math.acos(this.dot(b) / (mag * b.mag))
		if (result > Math.PI) result - 2 * Math.PI
		else result
	}

	override def equals(o: Any): Boolean = o match {
		case b: Vec3 => y == b.y && x == b.x && z == b.z
		case _ => false
	}

	override def toString: String = s"<$xDouble,$yDouble,$zDouble>"

	override def compareTo(b: Vec3): Int = java.lang.Double.compare(mag, b.mag)
}
