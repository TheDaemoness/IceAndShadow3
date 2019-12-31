package mod.iceandshadow3.lib.util.collect

import scala.collection.immutable
import scala.jdk.CollectionConverters._

/** A misbehaving set that represents a continuous [0, size) range of (boxed) Integers. */
class OrdSet(override val size: Int) extends immutable.Set[Integer] {
	override def incl(elem: Integer) = throw new UnsupportedOperationException
	override def excl(elem: Integer) = throw new UnsupportedOperationException

	override def contains(elem: Integer) = elem >= 0 && elem < size

	override def iterator: Iterator[Integer] = new Iterator[Integer] {
		private var counter = 0
		override def hasNext = counter < OrdSet.this.size
		override def next() = {counter += 1; counter-1}
	}

	def toJava = this.asJavaCollection
}
