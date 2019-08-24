package mod.iceandshadow3.lib.block

import javax.annotation.Nullable
import mod.iceandshadow3.lib.compat.block.impl.BinderBlockVar

import scala.collection.immutable

sealed abstract class BlockVar[T](val name: String) extends BinderBlockVar.TKey {
	def size: Int

	def parseHalf(value: String): Int
	def toString(value: Int): String

	def defaultIndex(): Int = 0
}

//TODO: Allow binding of dummies and make WBlockView know how to handle them.
//BlockVarPlaceholder will probably need to be in compat.block

/** Represents one variable of a block's overall state. Similar to IProperty. */
abstract class BBlockVar[T](name: String, values: T*) extends BlockVar[T](name) {
	private val to = immutable.Map.newBuilder[String, Int].addAll(values.map((_: T).toString).zipWithIndex).result()
	private val from = immutable.Map.newBuilder[T, Int].addAll(values.zipWithIndex).result()

	override def size = values.size

	@Nullable
	final def parse(value: String): Option[T] =
		to.get(value).fold[Option[T]](None)(idx => Some(values(idx)))

	final def parseHalf(value: String): Int = to.getOrElse(value, -1)

	def apply(value: T): Int = from.getOrElse(value, -1)
	def lookup(idx: Int) = values(idx)

	override def toString(value: Int) = lookup(value).toString
	
	BinderBlockVar.add(this)
}
