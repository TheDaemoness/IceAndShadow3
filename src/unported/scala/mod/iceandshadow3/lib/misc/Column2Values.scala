package mod.iceandshadow3.lib.misc

case class Column2Values[+T](end: T, side: T) extends Iterable[T] {
	def toColumn3Values = Column3Values[T](end, side, end)
	def toCubeValues = CubeValues[T](end, side, side, side, side, end)
	override def iterator = Iterator(end, side)
	override def foreach[U](f: T => U): Unit = {
		f(end)
		f(side)
	}
	override def map[B](f: T => B) = Column2Values(f(end), f(side))
}
object Column2Values {
	def apply[T](all: T): Column2Values[T] = Column2Values(all, all)
}
