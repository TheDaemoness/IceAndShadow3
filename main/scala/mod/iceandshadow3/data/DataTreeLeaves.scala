package mod.iceandshadow3.data

import net.minecraft.nbt._

import scala.collection.JavaConverters._

sealed trait TBounded[T <: AnyVal] {
	def getMin(): T
	def getMax(): T
	protected final def testLimits(tester: T => Boolean): Boolean = {
		Seq(getMin, getMax).prefixLength(tester) == 2
	}
}
sealed trait TIntBounded extends TBounded[Long] {
	def getMin(): Long
	def getMax(): Long
}
sealed trait TFloatBounded extends TBounded[Double] {
	def getMin(): Double = Double.MinValue
	def getMax(): Double = Double.MaxValue
}

class DatumBool(bool: Boolean) extends BDataTreeLeaf(bool) {
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getByte != 0
	override protected def writeNBT(value: Boolean) =
		new NBTTagByte(if(value) 1 else 0)
	override protected def parseLine(line: String) = java.lang.Boolean.valueOf(line)
}

abstract class DatumInt(int: Long) extends BDataTreeLeaf(int) with TIntBounded {
	override def set(newval: Long) = {
		if(newval < getMin || newval > getMax) false
		super.set(newval)
	}
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getLong
	override protected def writeNBT(value: Long) =
		if(testLimits {_.isValidByte}) new NBTTagByte(value.asInstanceOf[Byte])
		else if(testLimits {_.isValidShort}) new NBTTagShort(value.asInstanceOf[Short])
		else if(testLimits {_.isValidInt}) new NBTTagInt(value.asInstanceOf[Integer])
		else new NBTTagLong(value)
	override protected def parseLine(line: String) = java.lang.Long.valueOf(line)
}

class DatumString(string: String) extends BDataTreeLeaf(string) {
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTTagString].getString
	override protected def writeNBT(value: String) =
		new NBTTagString(value)
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
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getDouble
	override protected def writeNBT(value: Double) =
		if(emitFloat) new NBTTagFloat(value.asInstanceOf[Float]) else new NBTTagDouble(value)
	override protected def parseLine(line: String) = java.lang.Double.valueOf(line)
}

abstract class DatumIntArray(size: Int = 0) extends BDataTreeLeaf(new Array[Long](size)) with TIntBounded {
	override def set(newarray: Array[Long]) = {
		for(newval <- newarray) if(newval < getMin || newval > getMax) throw new IllegalArgumentException
		super.set(newarray)
	}
	override protected def readNBT(tag: INBTBase) = tag match {
		case bytes: NBTTagByteArray => bytes.getByteArray.map(_.toLong)
		case ints: NBTTagIntArray => ints.getIntArray.map(_.toLong)
		case _ => tag.asInstanceOf[NBTTagLongArray].getAsLongArray.toArray //ClassCastException and a forced copy
	}
	override protected def writeNBT(value: Array[Long]) = {
		if(testLimits {_.isValidByte}) new NBTTagByteArray(value.map(_.toByte))
		else if(testLimits {_.isValidInt}) new NBTTagIntArray(value.map(_.toInt))
		else new NBTTagLongArray(value)
	}
	override def toLine() = String.join("_", get().map(_.toString).toSeq.asJava)
	override protected def parseLine(line: String) = line.split(' ').map(java.lang.Long.valueOf(_).longValue)
}