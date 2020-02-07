package mod.iceandshadow3.lib.spatial

import mod.iceandshadow3.lib.data.CNVDataTree._
import mod.iceandshadow3.lib.data._
import mod.iceandshadow3.lib.spatial.IVec3._

class Vec3Mutable (
	private var x: Long,
	private var y: Int,
	private var z: Long
)
	extends IVec3
	with ITextLineRW
	with IDataTreeRW[DataTreeMap]
{
	def this(from: IVec3) = this(from.xRaw, from.yRaw, from.zRaw)
	override def xRaw = x
	override def yRaw = y
	override def zRaw = z
	override def asMutable: Vec3Mutable = this

  def set(b: IVec3): Unit = {
		this.x = b.xRaw
		this.y = b.yRaw
		this.z = b.zRaw
	}

  def add(x: Int, y: Int, z: Int): Vec3Mutable = {
		this.x += x << SUB_BITS
		this.y += y.toShort << SUB_BITS
		this.z += z << SUB_BITS
		this
	}

	def add(x: Double, y: Double, z: Double): Vec3Mutable = {
		this.x += IVec3.fromDouble(x)
		this.y += IVec3.fromDouble(y).toInt
		this.z += IVec3.fromDouble(z)
		this
	}

	def add(b: IVec3): Vec3Mutable = {
		x += b.xRaw
		y += b.yRaw
		z += b.zRaw
		this
	}

	def sub(b: IVec3): Vec3Mutable = {
		x -= b.xRaw
		y -= b.yRaw
		z -= b.zRaw
		this
	}

	def mult(multiplier: Double): Vec3Mutable = {
		x = (x.toDouble*multiplier).toLong
		y = (y.toDouble*multiplier).toInt
		z = (z.toDouble*multiplier).toLong
		this
	}

/// Converts the vector to its normal equivalent (magnitude = 1).
	def norm(): Vec3Mutable = mult(1 / mag)

	def inv(): Vec3Mutable = mult(-1)
	
	override def fromLine(line: String): Unit = {
		val scanner = new java.util.Scanner(line)
		this.x = scanner.nextLong
		this.y = scanner.nextInt
		this.z = scanner.nextLong
	}
	
	override def toLine: String = s"$xDouble $yDouble $zDouble"
	
	def fromDataTree(tree: DataTreeMap): Boolean = {
		try {
			this.x = tree.getAndUnwrap[Long, DatumInt64]("x").get
			this.y = tree.getAndUnwrap[Long, DatumInt32]("y").get.toInt
			this.z = tree.getAndUnwrap[Long, DatumInt64]("z").get
		} catch {
			case _: NoSuchElementException => return false
		}
		true
	}

	def exposeDataTree(): DataTreeMap =
		new DataTreeMap().add("x", x).add("y", y).add("z", z)
}