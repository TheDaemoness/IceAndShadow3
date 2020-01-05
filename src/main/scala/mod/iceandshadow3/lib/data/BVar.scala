package mod.iceandshadow3.lib.data

abstract class BVar[T](val name: String) {
	private[data] val numericId: Int = BVar.synchronized(BVar.nextId)
	def defaultVal: T
	/** Used to optimize some serialization operations. It may be safely kept false. */
	def isDefaultValue(value: T) = false
	override val hashCode = name.hashCode
}
object BVar {
	private var counter = Int.MinValue
	private[data] def nextId: Int = {
		counter += 1
		counter
	}
	val ordering = new Ordering[BVar[_]] {
		override def compare(x: BVar[_], y: BVar[_]) = x.numericId.compareTo(y.numericId)
	}
}
