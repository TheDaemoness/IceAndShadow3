package mod.iceandshadow3.util.collect

/** An immutable wrapper for a single element, pretending to be a sequence of [n,posinf) such identical elements.
	*/
class UniSeq[+T](val length: Int, value: T) extends Seq[T] {
	override def apply(idx: Int) = value
	override def iterator = new Iterator[T] {
		private var counter = 0
		override def hasNext = counter < length
		override def next() = {counter += 1; value}
	}
}
