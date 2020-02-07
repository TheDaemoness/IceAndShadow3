package mod.iceandshadow3.lib.data

abstract class Var[T](val name: String) {
	private[data] val numericId: Int = Var.synchronized(Var.nextId)
	def defaultVal: T
	/** Used to optimize some serialization operations. It may be safely kept false. */
	def isDefaultValue(value: T) = false
	override val hashCode = name.hashCode
}
object Var {
	private var counter = Int.MinValue
	private[data] def nextId: Int = {
		counter += 1
		counter
	}
	val ordering = new Ordering[Var[_]] {
		override def compare(x: Var[_], y: Var[_]) = x.numericId.compareTo(y.numericId)
	}
}
