package mod.iceandshadow3.lib.util

import scala.collection.immutable

class OrdSet(override val size: Int) extends immutable.Set[Integer] {
	override def incl(elem: Integer) = throw new UnsupportedOperationException
	override def excl(elem: Integer) = throw new UnsupportedOperationException

	override def contains(elem: Integer) = elem >= 0 && elem < size

	override def iterator: Iterator[Integer] = new Iterator[Integer] {
		private var counter = 0
		override def hasNext = counter < OrdSet.this.size
		override def next() = {counter += 1; counter-1}
	}
}
