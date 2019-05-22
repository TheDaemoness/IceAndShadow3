package mod.iceandshadow3.util

import mod.iceandshadow3.data.{DataTreeMap, DatumInt32, DatumInt64, IDataTreeRW, ITextLineRW}

class Vec3M(original: Vec3) extends Vec3(original.xRaw, original.yRaw, original.zRaw)
	with ITextLineRW
	with IDataTreeRW[DataTreeMap]
{
	override def getMutable: Vec3M = this

  def set(b: Vec3): Unit = {
		this.x = b.xRaw
		this.y = b.yRaw
		this.z = b.zRaw
	}

  def add(x: Int, y: Int, z: Int): Vec3M = {
		this.x += x << Vec3.SUB_BITS
		this.y += y.toShort << Vec3.SUB_BITS
		this.z += z << Vec3.SUB_BITS
		this
	}

	def add(x: Double, y: Double, z: Double): Vec3M = {
		this.x += Vec3.toLong(x)
		this.y += Vec3.toLong(y).toInt
		this.z += Vec3.toLong(z)
		this
	}

	def add(b: Vec3): Vec3M = {
		x += b.xRaw
		y += b.yRaw
		z += b.zRaw
		this
	}

	def sub(b: Vec3): Vec3M = {
		x -= b.xRaw
		y -= b.yRaw
		z -= b.zRaw
		this
	}

	def mult(multiplier: Double): Vec3M = {
		x = (x.toDouble*multiplier).toLong
		y = (y.toDouble*multiplier).toInt
		z = (z.toDouble*multiplier).toLong
		this
	}

/// Converts the vector to its normal equivalent (magnitude = 1).
	def norm(): Vec3M = mult(1 / mag)

	def inv(): Vec3M = mult(-1)
	
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
			case e: NoSuchElementException => return false
		}
		true
	}

	import mod.iceandshadow3.data.SDataTreeConversions._
	def exposeDataTree(): mod.iceandshadow3.data.DataTreeMap =
		new DataTreeMap().add("x", x).add("y", y).add("z", z)
}