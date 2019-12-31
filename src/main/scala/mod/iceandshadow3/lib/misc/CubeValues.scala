package mod.iceandshadow3.lib.misc

case class CubeValues[+T](top: T, front: T, left: T, right: T, back: T, bottom: T)
extends Iterable[T] {
	def toColumn2Values = Column2Values[T](top, back)
	def toColumn3Values = Column3Values[T](top, back, bottom)
	def equalSides = front == left && left == right && right == back
	def equalEnds = top == bottom
	def equalFaces = equalSides && equalEnds && top == front
	override def iterator = Iterator(top, front, left, right, back, bottom)
	override def foreach[U](f: T => U): Unit = {
		f(top)
		f(front)
		f(left)
		f(right)
		f(back)
		f(bottom)
	}
	override def map[B](f: T => B) = CubeValues(f(top), f(front), f(left), f(right), f(back), f(bottom))
}
object CubeValues {
	final class Builder[T](default: T) {
		private var _top = default
		private var _front = default
		private var _left = default
		private var _right = default
		private var _back = default
		private var _bottom = default
		def top(value: T): this.type = {_top = value; this}
		def bottom(value: T): this.type = {_bottom = value; this}
		def front(value: T): this.type = {_front = value; this}
		def left(value: T): this.type = {_left = value; this}
		def right(value: T): this.type = {_right = value; this}
		def back(value: T): this.type = {_back = value; this}

		def sides(value: T): this.type = front(value).back(value).left(value).right(value)
		def ends(value: T): this.type = top(value).bottom(value)
		def result: CubeValues[T] = CubeValues(_top, _front, _left, _right, _back, _bottom)
	}
	def builder[T](default: T) = new Builder[T](default)
}
