package mod.iceandshadow3.lib.block

import mod.iceandshadow3.lib.compat.block.impl.BBlockVarNew

import scala.collection.immutable

class BlockVar[T](name: String, values: T*) extends BBlockVarNew[T](name) {
	private val to = immutable.Map.newBuilder[String, Int].addAll(values.map((_: T).toString).zipWithIndex).result()
	private val from = immutable.Map.newBuilder[T, Int].addAll(values.zipWithIndex).result()
	override def size = values.size

	override def defaultValue: T = values(0)
	override def toThem(value: T): Integer = Integer.valueOf(from.getOrElse(value, -1))
	override def toUs(idx: Integer) = values(idx)

	override def fromString(value: String): Int = to.getOrElse(value, -1)
	override def toString(value: Int) = toUs(value).toString
}
