package mod.iceandshadow3.lib.util.collect

/** Iterator that, given a Int => T and a length, "iterates" over it by calling apply with increasing values.
	* Used for quick and dirty Seq implementations.
	*/
class IteratorSeq[+T](gen: Int => T, length: Int) extends Iterator[T] {
	def this(seq: Seq[T]) = this(seq, seq.length)
	private var count = -1
	override def hasNext = count < length-1
	override def next() = {
		if(!hasNext) throw new NoSuchElementException(s"$this($gen, $length) has reached its end")
		count += 1
		gen(count)
	}
}
