package mod.iceandshadow3.data

import net.minecraft.nbt._

import scala.collection.JavaConverters._

// Rule: When something explodes here, it throws a ClassCastException or IllegalArgumentException.

abstract class BDataTree[Value](protected var datum: Value) extends INbtSerializable with IDataTreeSerializable[BDataTree[Value]] {
	def get(): Value = datum
	
	protected def writeNBT(value: Value): INBTBase
	
	override final def toNBT(): INBTBase = writeNBT(get())
	def fromNBT(tag: INBTBase): Unit
	
	override final def getDataTree() = this
}

// TODO: Impose type restrictions on Container.
abstract class BDataTreeBranch[Container](c: Container) extends BDataTree(c)  {
	protected def copyFrom(c: Container)
	override final def fromDataTree(newval: BDataTree[Container]) = if(newval != this) copyFrom(newval.get())
}

abstract class BDataTreeLeaf[Value](v: Value) extends BDataTree(v) with ILineSerializable {
	def set(value: Value) = {datum = value}
	override final def fromDataTree(newval: BDataTree[Value]) = if(newval != this) set(newval.get())
	
	override final def fromNBT(tag: INBTBase): Unit = set(readNBT(tag))
	protected def readNBT(tag: INBTBase): Value
	
	protected def parseLine(line: String): Value
	
	def toLine(): String = datum.toString
	final def fromLine(line: String) = set(parseLine(line))
}

sealed trait TBounded[T <: AnyVal] {
	def getMin(): T
	def getMax(): T
	protected final def testLimits(tester: T => Boolean): Boolean = {
		Seq(getMin, getMax).prefixLength(tester) == 2
	}
}
trait TIntBounded extends TBounded[Long] {
	def getMin(): Long = Long.MinValue
	def getMax(): Long = Long.MaxValue
}
trait TFloatBounded extends TBounded[Double] {
	def getMin(): Double = Double.MinValue
	def getMax(): Double = Double.MaxValue
}

class DatumBool(bool: Boolean) extends BDataTreeLeaf(bool) {
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getByte != 0
	override protected def writeNBT(value: Boolean) =
		new NBTTagByte(if(bool) 1 else 0)
	override protected def parseLine(line: String) = java.lang.Boolean.valueOf(line)
}

class DatumInt(int: Long) extends BDataTreeLeaf(int) with TIntBounded {
	override def set(newval: Long) = {
		if(newval < getMin || newval > getMax) throw new IllegalArgumentException
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
		if(newval < getMin || newval > getMax) throw new IllegalArgumentException
		super.set(newval)
	}
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getDouble
	override protected def writeNBT(value: Double) =
		if(emitFloat) new NBTTagFloat(value.asInstanceOf[Float]) else new NBTTagDouble(value)
	override protected def parseLine(line: String) = java.lang.Double.valueOf(line)
}

class DatumIntArray(size: Int = 0) extends BDataTreeLeaf(new Array[Long](size)) with TIntBounded {
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

class DataTreeList[Element <: BDataTree[_]] extends BDataTreeBranch[java.util.List[Element]](new java.util.LinkedList) {
	override def fromNBT(tag: INBTBase) = {
		val list = tag.asInstanceOf[NBTTagList]
		for ((elem, i) <- (datum.asScala zip (0 to list.size-1))) elem.fromNBT(list.get(i))
	}
	override protected def writeNBT(list: java.util.List[Element]) = {
		val retval = new NBTTagList()
		for(elem <- list.asScala) retval.add(elem.toNBT)
		retval
	}
	override protected def copyFrom(list: java.util.List[Element]) =
		datum = new java.util.LinkedList(list)
}

class DataTreeMap extends BDataTreeBranch[java.util.Map[String,BDataTree[_]]](new java.util.HashMap) {
	override def fromNBT(tag: INBTBase) = {
		val compound = tag.asInstanceOf[NBTTagCompound]
		for(pair <- datum.entrySet.asScala) pair.getValue.fromNBT(compound.getTag(pair.getKey))
	}
	override protected def writeNBT(map: java.util.Map[String,BDataTree[_]]) = {
		val retval = new NBTTagCompound()
		for(pair <- map.entrySet.asScala) retval.setTag(pair.getKey, pair.getValue.toNBT)
		retval
	}
	override protected def copyFrom(map: java.util.Map[String,BDataTree[_]]) =
		datum = new java.util.HashMap(map)
}