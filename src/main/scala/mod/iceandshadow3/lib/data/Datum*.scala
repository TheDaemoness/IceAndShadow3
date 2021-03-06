package mod.iceandshadow3.lib.data

import net.minecraft.nbt._

import scala.jdk.CollectionConverters._

sealed trait TBounded[T <: AnyVal] {
	def getMin: T
	def getMax: T
	protected final def testLimits(tester: T => Boolean): Boolean = {
		Seq(getMin, getMax).forall(tester)
	}
}
sealed trait TIntBounded extends TBounded[Long] {
	def getMin: Long
	def getMax: Long
}
sealed trait TFloatBounded extends TBounded[Double] {
	def getMin: Double = Double.MinValue
	def getMax: Double = Double.MaxValue
}

class DatumBool(bool: Boolean) extends DataTreeLeaf(bool) {
	def isTrue: Boolean = get
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[NumberNBT].getByte != 0
	override protected def writeNBT(value: Boolean) = ByteNBT.func_229672_a_(value)
	override protected def parseLine(line: String) = java.lang.Boolean.valueOf(line)
}

abstract class DatumInt(int: Long) extends DataTreeLeaf(int) with TIntBounded {
	override def set(newval: Long) = {
		if(newval < getMin || newval > getMax) false
		else super.set(newval)
	}
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[NumberNBT].getLong
	override protected def writeNBT(value: Long) =
		if(testLimits {_.isValidByte}) ByteNBT.func_229671_a_(value.toByte)
		else if(testLimits {_.isValidShort}) ShortNBT.func_229701_a_(value.toShort)
		else if(testLimits {_.isValidInt}) IntNBT.func_229692_a_(value.toInt)
		else LongNBT.func_229698_a_(value)
	override protected def parseLine(line: String) = java.lang.Long.valueOf(line)
}

class DatumString(string: String) extends DataTreeLeaf(string) {
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[StringNBT].getString
	override protected def writeNBT(value: String) =
		StringNBT.func_229705_a_(value)
	override protected def parseLine(line: String) = line //TODO: Decide if we need quotes.
}

class DatumFloat(
	double: Double, 
	protected val emitFloat: Boolean = false
) extends DataTreeLeaf(double) with TFloatBounded {
	override def set(newval: Double) = {
		if(newval < getMin || newval > getMax) false
		else super.set(newval)
	}
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[NumberNBT].getDouble
	override protected def writeNBT(value: Double) =
		if(emitFloat) FloatNBT.func_229689_a_(value.toFloat) else DoubleNBT.func_229684_a_(value)
	override protected def parseLine(line: String) = java.lang.Double.valueOf(line)
}

abstract class DatumIntArray(size: Int = 0) extends DataTreeLeaf(new Array[Long](size)) with TIntBounded {
	override def set(newarray: Array[Long]) = {
		for(newval <- newarray) if(newval < getMin || newval > getMax) throw new IllegalArgumentException
		super.set(newarray)
	}
	def set(newarray: Array[Int]): Boolean = {
		this.set(newarray.map{_.toLong})
	}
	def set(newarray: Array[Short]): Boolean = {
		this.set(newarray.map{_.toLong})
	}
	def set(newarray: Array[Byte]): Boolean = {
		this.set(newarray.map{_.toLong})
	}
	override protected def readNBT(tag: INBT) = tag match {
		case bytes: ByteArrayNBT => bytes.getByteArray.map(_.toLong)
		case ints: IntArrayNBT => ints.getIntArray.map(_.toLong)
		case _ => Array.from(tag.asInstanceOf[LongArrayNBT].getAsLongArray) //Copy deliberate.
	}
	override protected def writeNBT(value: Array[Long]) = {
		if(testLimits {_.isValidByte}) new ByteArrayNBT(value.map(_.toByte))
		else if(testLimits {_.isValidInt}) new IntArrayNBT(value.map(_.toInt))
		else new LongArrayNBT(value)
	}
	override def toLine = String.join("_", get.map(_.toString).toSeq.asJava)
	override protected def parseLine(line: String) = line.split(' ').map(java.lang.Long.valueOf(_).longValue)
}