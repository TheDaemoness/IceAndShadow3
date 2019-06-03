package mod.iceandshadow3.util

class IteratorEmpty[E] extends java.util.Iterator[E] with Iterator[E] {
	override def hasNext() = false
	override def next(): E = throw new NoSuchElementException("next called on EmptyIterator")
}