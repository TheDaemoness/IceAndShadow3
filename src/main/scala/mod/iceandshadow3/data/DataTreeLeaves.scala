package mod.iceandshadow3.data

import net.minecraft.nbt._

import scala.collection.JavaConverters._

sealed trait TBounded[T <: AnyVal] {
	def getMin: T
	def getMax: T
	protected final def testLimits(tester: T => Boolean): Boolean = {
		Seq(getMin, getMax).prefixLength(tester) == 2
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

class DatumBool(bool: Boolean) extends BDataTreeLeaf(bool) {
	def isTrue: Boolean = get
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[NumberNBT].getByte != 0
	override protected def writeNBT(value: Boolean) =
		new ByteNBT(if(value) 1 else 0)
	override protected def parseLine(line: String) = java.lang.Boolean.valueOf(line)
}

abstract class DatumInt(int: Long) extends BDataTreeLeaf(int) with TIntBounded {
	override def set(newval: Long) = {
		if(newval < getMin || newval > getMax) false
		super.set(newval)
	}
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[NumberNBT].getLong
	override protected def writeNBT(value: Long) =
		if(testLimits {_.isValidByte}) new ByteNBT(value.toByte)
		else if(testLimits {_.isValidShort}) new ShortNBT(value.toShort)
		else if(testLimits {_.isValidInt}) new IntNBT(value.toInt)
		else new LongNBT(value)
	override protected def parseLine(line: String) = java.lang.Long.valueOf(line)
}

class DatumString(string: String) extends BDataTreeLeaf(string) {
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[StringNBT].getString
	override protected def writeNBT(value: String) =
		new StringNBT(value)
	override protected def parseLine(line: String) = line //TODO: Decide if we need quotes.
}

class DatumFloat(
	double: Double, 
	protected val emitFloat: Boolean = false
) extends BDataTreeLeaf(double) with TFloatBounded {
	override def set(newval: Double) = {
		if(newval < getMin || newval > getMax) false
		super.set(newval)
	}
	override protected def readNBT(tag: INBT) =
		tag.asInstanceOf[NumberNBT].getDouble
	override protected def writeNBT(value: Double) =
		if(emitFloat) new FloatNBT(value.toFloat) else new DoubleNBT(value)
	override protected def parseLine(line: String) = java.lang.Double.valueOf(line)
}

abstract class DatumIntArray(size: Int = 0) extends BDataTreeLeaf(new Array[Long](size)) with TIntBounded {
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
		case _ => tag.asInstanceOf[LongArrayNBT].getAsLongArray.toArray //the toArray is deliberate.
	}
	override protected def writeNBT(value: Array[Long]) = {
		if(testLimits {_.isValidByte}) new ByteArrayNBT(value.map(_.toByte))
		else if(testLimits {_.isValidInt}) new IntArrayNBT(value.map(_.toInt))
		else new LongArrayNBT(value)
	}
	override def toLine = String.join("_", get.map(_.toString).toSeq.asJava)
	override protected def parseLine(line: String) = line.split(' ').map(java.lang.Long.valueOf(_).longValue)
}