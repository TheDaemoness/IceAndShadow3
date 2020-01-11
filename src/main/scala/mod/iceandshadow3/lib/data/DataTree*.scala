package mod.iceandshadow3.lib.data

import mod.iceandshadow3.lib.util.GeneralUtils
import net.minecraft.nbt._

import scala.reflect.ClassTag

abstract class DataTree[Value](protected var datum: Value) extends IDataTreeRW[DataTree[Value]] {
	def get: Value = datum

	protected[lib] final def toNBT: INBT = writeNBT(get)
	@throws[ClassCastException]
	@throws[IllegalArgumentException]
	protected[lib] def fromNBT(tag: INBT): Boolean

	protected def writeNBT(value: Value): INBT

	override final def exposeDataTree() = this
}

abstract class DataTreeBranch[Container, Key](c: Container) extends DataTree(c) with Iterable[Key Tuple2 IDataTreeRW[_]] {
	protected def copyFrom(c: Container): Unit
	override final def fromDataTree(newval: DataTree[Container]) = {if(newval != this) copyFrom(newval.get); true}

	def get(key: Key): Option[IDataTreeRW[_ <: DataTree[_]]]

	protected def getForRead(key: Key): Option[IDataTreeRW[_ <: DataTree[_]]] = get(key)

	def getAndCast[T <: IDataTreeRW[_ <: DataTree[_]]: ClassTag](key: Key): Option[T] = {
		val getopt = get(key)
		if(getopt.isEmpty) None else GeneralUtils.cast[T](getopt.get)
	}
	def getAndUnwrap[V, T <: DataTree[V]: ClassTag](key: Key): Option[V] = {
		val wrappedopt: Option[T] = getAndCast[T](key)
		if(wrappedopt.isEmpty) None else Some(wrappedopt.get.get)
	}
}

abstract class DataTreeLeaf[Value](v: Value) extends DataTree(v) with ITextLineRW {
	def set(value: Value): Boolean = {datum = value; true}
	override final def fromDataTree(newval: DataTree[Value]) = if(newval != this) set(newval.get) else true
	
	override final def fromNBT(tag: INBT): Boolean = set(readNBT(tag))
	protected def readNBT(tag: INBT): Value
	
	protected def parseLine(line: String): Value

	def toLine: String = datum.toString
	final def fromLine(line: String): Unit = set(parseLine(line))

	override def toString = datum.toString
}