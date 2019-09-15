package mod.iceandshadow3.lib.data

import mod.iceandshadow3.lib.util.Casting
import net.minecraft.nbt._

import scala.reflect.ClassTag

abstract class BDataTree[Value](protected var datum: Value) extends IDataTreeRW[BDataTree[Value]] {
	def get: Value = datum

	protected[lib] final def toNBT: INBT = writeNBT(get)
	@throws[ClassCastException]
	@throws[IllegalArgumentException]
	protected[lib] def fromNBT(tag: INBT): Boolean

	protected def writeNBT(value: Value): INBT
	
	override final def exposeDataTree() = this
}

abstract class BDataTreeBranch[Container, Key](c: Container) extends BDataTree(c) with Iterable[Key Tuple2 IDataTreeRW[_]] {
	protected def copyFrom(c: Container): Unit
	override final def fromDataTree(newval: BDataTree[Container]) = {if(newval != this) copyFrom(newval.get); true}
	
	def get(key: Key): Option[IDataTreeRW[_ <: BDataTree[_]]]

	protected def getForRead(key: Key): Option[IDataTreeRW[_ <: BDataTree[_]]] = get(key)

	def getAndCast[T <: IDataTreeRW[_ <: BDataTree[_]]: ClassTag](key: Key): Option[T] = {
		val getopt = get(key)
		if(getopt.isEmpty) None else Casting.cast[T](getopt.get)
	}
	def getAndUnwrap[V, T <: BDataTree[V]: ClassTag](key: Key): Option[V] = {
		val wrappedopt: Option[T] = getAndCast[T](key)
		if(wrappedopt.isEmpty) None else Some(wrappedopt.get.get)
	}
}

abstract class BDataTreeLeaf[Value](v: Value) extends BDataTree(v) with ITextLineRW {
	def set(value: Value): Boolean = {datum = value; true}
	override final def fromDataTree(newval: BDataTree[Value]) = if(newval != this) set(newval.get) else true
	
	override final def fromNBT(tag: INBT): Boolean = set(readNBT(tag))
	protected def readNBT(tag: INBT): Value
	
	protected def parseLine(line: String): Value

	def toLine: String = datum.toString
	final def fromLine(line: String): Unit = set(parseLine(line))

	override def toString = datum.toString
}