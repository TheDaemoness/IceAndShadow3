package mod.iceandshadow3.data

import net.minecraft.nbt._

import scala.collection.JavaConverters._

// Rule: When something explodes here, it throws a ClassCastException or IllegalArgumentException..

sealed trait TDataTreeIntegral {
	def getMin(): Long = Long.MinValue
	def getMax(): Long = Long.MaxValue
	protected final def testLimits(tester: Long => Boolean): Boolean = {
		Seq(getMin, getMax).prefixLength(tester) == 2
	}
}

abstract class BDataTree[Value](protected var datum: Value) extends INbtSerializable {
	def get(): Value = datum
	
	protected def writeNBT(value: Value): INBTBase
	
	override final def toNBT(): INBTBase = writeNBT(get())
	def fromNBT(tag: INBTBase): Unit
}
abstract class BDataTreeBranch[Container](c: Container) extends BDataTree(c) {
	
}
abstract class BDataTreeLeaf[Value](v: Value) extends BDataTree(v) {
	def set(value: Value) = {datum = value}
	override final def fromNBT(tag: INBTBase): Unit = set(readNBT(tag))
	protected def readNBT(tag: INBTBase): Value
}
class DataTreeBool(bool: Boolean) extends BDataTreeLeaf(bool) {
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getByte != 0
	override protected def writeNBT(value: Boolean) =
		new NBTTagByte(if(bool) 1 else 0)
		
}
class DataTreeIntegral(int: Long) extends BDataTreeLeaf(int) with TDataTreeIntegral {
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
}

class DataTreeString(string: String) extends BDataTreeLeaf(string) {
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTTagString].getString
	override protected def writeNBT(value: String) =
		new NBTTagString(value)
}

class DataTreeFloatingPoint(double: Double, protected val emitLowPrecision: Boolean = false) extends BDataTreeLeaf(double) {
	override protected def readNBT(tag: INBTBase) =
		tag.asInstanceOf[NBTPrimitive].getDouble
	override protected def writeNBT(value: Double) =
		if(emitLowPrecision) new NBTTagFloat(value.asInstanceOf[Float]) else new NBTTagDouble(value)
}
class DataTreeIntegralArray(size: Int = 0) extends BDataTreeLeaf(new Array[Long](size)) with TDataTreeIntegral {
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
}

class DataTreeList[Element <: BDataTree[_]] extends BDataTreeBranch[java.util.List[Element]](new java.util.LinkedList[Element]) {
	override def fromNBT(tag: INBTBase) = {
		val list = tag.asInstanceOf[NBTTagList]
		for ((elem, i) <- (datum.asScala zip (0 to list.size-1))) elem.fromNBT(list.get(i))
	}
	override protected def writeNBT(list: java.util.List[Element]) = {
		val retval = new NBTTagList()
		for(elem <- list.asScala) retval.add(elem.toNBT)
		retval
	}
}

class DataTreeMap extends BDataTreeBranch(new java.util.HashMap[String,BDataTree[_]]) {
	override def fromNBT(tag: INBTBase) = {
		val compound = tag.asInstanceOf[NBTTagCompound]
		for(pair <- datum.entrySet.asScala) pair.getValue.fromNBT(compound.getTag(pair.getKey))
	}
	override protected def writeNBT(map: java.util.HashMap[String,BDataTree[_]]) = {
		val retval = new NBTTagCompound()
		for(pair <- map.entrySet.asScala) retval.setTag(pair.getKey, pair.getValue.toNBT)
		retval
	}
}