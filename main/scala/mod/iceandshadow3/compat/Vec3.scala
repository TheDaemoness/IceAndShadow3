package mod.iceandshadow3.compat

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos

import Vec3._

object Vec3 {
	protected[compat] val SUB_BITS: Long = 16
	protected val CHUNK_BITS: Long = 4
	protected val SUB_MULT: Long = 1 << SUB_BITS
	protected val CHUNK_MULT: Long = 1 << CHUNK_BITS
	protected val SUB_MASK: Long = SUB_MULT - 1
	protected val CHUNK_MASK: Long = CHUNK_MULT - 1

	protected[compat] def toLong(value: Double): Long = {
		val whole: Long = value.toLong
		(whole << SUB_BITS) + ((value - whole) * SUB_MULT).toLong
	}

	protected def toBlockCoord(value: Long): Long = value >> SUB_BITS
	protected def toSubCoord(value: Long): Int = (value & SUB_MASK).toInt
	protected[compat] def toDouble(value: Long): Double =
		toBlockCoord(value) + toSubCoord(value).toDouble / SUB_MULT

	// Unit vectors.
	val U: Vec3 = new Vec3(0, 1, 0)
	val D: Vec3 = new Vec3(0, -1, 0)
	val S: Vec3 = new Vec3(0, 0, 1)
	val N: Vec3 = new Vec3(0, 0, -1)
	val E: Vec3 = new Vec3(1, 0, 0)
	val W: Vec3 = new Vec3(-1, 0, 0)
}

/**
* An immutable fixed-precision 3d vector class.
*/
class Vec3 protected (
	protected[compat] var x: Long,
	protected[compat] var y: Int,
	protected[compat] var z: Long
) extends Comparable[Vec3] with Cloneable {
	protected def this(x: Long, y: Int, z: Long, shift: Long) = {
		this(x << shift, y << shift, z << shift)
	}
	def this(x: Int, y: Int, z: Int) = this(x, y, z, SUB_BITS)
	def this(p: BlockPos) = this(p.getX(), p.getY(), p.getZ(), SUB_BITS)
	def this(e: Entity) = this(toLong(e.posX), toLong(e.posY).toInt, toLong(e.posZ), 0)

	def xBlock(): Long = toBlockCoord(x)
	def yBlock(): Int = toBlockCoord(y).toInt
	def zBlock(): Long = toBlockCoord(z)
	def xChunk(): Int = (toBlockCoord(x) >> CHUNK_BITS).toInt
	def zChunk(): Int = (toBlockCoord(z) >> CHUNK_BITS).toInt
	def xSubChunk(): Byte = (toBlockCoord(x) & CHUNK_MASK).toByte
	def zSubChunk(): Byte = (toBlockCoord(z) & CHUNK_MASK).toByte
	def xSubBlock(): Int = toSubCoord(x)
	def ySubBlock(): Int = toSubCoord(y)
	def zSubBlock(): Int = toSubCoord(z)
	def xDouble(): Double = toDouble(x)
	def yDouble(): Double = toDouble(y)
	def zDouble(): Double = toDouble(z)

/// Returns a mutable copy of this vector.
	def copy(): Vec3M = new Vec3M(x, y, z)
	def mag(): Double = Math.sqrt(x * x + y.toLong * y + z * z)
	def magH(): Double = Math.sqrt(x * x + z * z)

	def dot(b: Vec3): Double = toDouble(x * b.x + y.toLong * b.y + z * b.z)
	def toBlockPos(): BlockPos = new BlockPos(xBlock(), yBlock(), zBlock())

	override def equals(o: Any): Boolean = {
		if (o.isInstanceOf[Vec3]) {
			val b: Vec3 = o.asInstanceOf[Vec3]
			y == b.y && x == b.x && z == b.z
		} else false
	}

	override def toString(): String = s"<$xDouble,$yDouble,$zDouble()>"

	override def compareTo(b: Vec3): Int = java.lang.Double.compare(mag(), b.mag())
}

