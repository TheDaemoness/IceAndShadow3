package mod.iceandshadow3.lib.util.collect

/** A Scala iterator that wraps Java iterators whose outputs are seamlessly concatenated and transformed by a function.
  * Iterables.concat does exist, but Scala seems to dislike it.
  */
class IteratorConcat[In, Out](
  converter: In => Out,
  iters: java.util.Iterator[In] *
) extends Iterator[Out] {
  private val listiter = iters.iterator
  private var currentiter = listiter.next

  override def hasNext: Boolean = {
    while (!currentiter.hasNext && listiter.hasNext) currentiter = listiter.next
    currentiter.hasNext
  }

  override def next: Out = {
    if(hasNext) converter(currentiter.next) else null.asInstanceOf[Out]
  }
}