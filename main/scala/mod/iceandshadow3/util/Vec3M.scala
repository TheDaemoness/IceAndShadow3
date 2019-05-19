package mod.iceandshadow3.util

import mod.iceandshadow3.data.{DataTreeMap, DatumInt32, DatumInt64, IDataTreeRW, ITextLineRW}
import mod.iceandshadow3.data.SDataTreeConversions._

class Vec3M(xO: Long, yO: Int, zO: Long) extends Vec3(xO, yO, zO) with ITextLineRW with IDataTreeRW[DataTreeMap] {
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
	
	override def toLine: String = "$x $y $z"
	
	def fromDataTree(tree: DataTreeMap): Boolean = {
		try {
			this.x = SCaster.cast[DatumInt64](tree.get("x")).get.get
			this.y = SCaster.cast[DatumInt32](tree.get("y")).get.get.asInstanceOf[Int]
			this.z = SCaster.cast[DatumInt64](tree.get("z")).get.get
		} catch {
			case e: NoSuchElementException => return false
		}
		true
	}
	def newDataTree(): mod.iceandshadow3.data.DataTreeMap =
		new DataTreeMap().add("x", x).add("y", y).add("z", z)
}