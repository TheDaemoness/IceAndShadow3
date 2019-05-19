package mod.iceandshadow3.util

import java.util.Collection

/** A somewhat misbehaving set that never contains any elements.
	*/
class EmptySet[E <: Object] extends java.util.Set[E] with Iterable[E] {
	override def size = 0
	override def isEmpty = true
	override def contains(o: Any) = false
	override def iterator = new EmptyIterator[E]
	override def toArray = new Array[AnyRef](0)
	override def add(e: E) = true
	override def remove(o: Any) = false
	override def containsAll(collection: Collection[_]) = false
	override def addAll(collection: Collection[_ <: E]) = false
	override def retainAll(collection: Collection[_]) = false
	override def removeAll(collection: Collection[_]) = false
	override def clear(): Unit = {}
	override def hashCode = 0

	def toArray[T](ts: Array[T]): Array[T] = ts //Shuts up the IDEA linter while still compiling.
	def toArray[T](ts: Array[T with Object]): Array[T with Object] = ts
	override def equals(o: Any): Boolean = o match {
		case es: java.util.Set[E] => es.isEmpty
		case _ => false
	}
}