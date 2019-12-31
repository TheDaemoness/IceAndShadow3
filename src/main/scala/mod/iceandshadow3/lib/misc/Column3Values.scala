package mod.iceandshadow3.lib.misc

case class Column3Values[+T](top: T, side: T, bottom: T) extends Iterable[T] {
	def toColumn2Values = Column2Values[T](top, side)
	def toCubeValues = CubeValues[T](top, side, side, side, side, bottom)
	override def iterator = Iterator(top, side, bottom)
	override def foreach[U](f: T => U): Unit = {
		f(top)
		f(side)
		f(bottom)
	}
	override def map[B](f: T => B) = Column3Values(f(top), f(side), f(bottom))
}
object Column3Values {
	def apply[T](ends: T, side: T): Column3Values[T] = Column3Values(ends, side, ends)
	def apply[T](all: T): Column3Values[T] = Column3Values(all, all, all)
}
