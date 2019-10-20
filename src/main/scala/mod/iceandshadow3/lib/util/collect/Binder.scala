package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.IaS3
import scala.reflect.ClassTag

class Binder[KeyType: ClassTag, ValueType <: Object: ClassTag] extends Iterable[ValueType] {
	trait TKey {
		this: KeyType =>
		private[Binder] var binderIndex: Int = -1
	}
	private var mutable = new scala.collection.mutable.HashMap[TKey, ValueType]()
	private var immutable: Array[ValueType] = Array.empty

	def frozen = mutable == null
	def freeze(): Array[ValueType] = {
		immutable = new Array[ValueType](mutable.size)
		val iterator = mutable.iterator
		var i = 0
		while(iterator.hasNext) {
			val (key, value) = iterator.next()
			key.binderIndex = i
			immutable(i) = value
			i += 1
		}
		mutable = null
		immutable
	}
	final def add(key: TKey, value: ValueType): Unit = {
		if(mutable != null) mutable.put(key, value)
		else IaS3.bug(key, s"$this.add($key, $value) called after freeze")
	}
	def apply(key: TKey): ValueType = {
		try {
			if(mutable == null) immutable(key.binderIndex) else mutable(key)
		} catch {
			case _: ArrayIndexOutOfBoundsException => onUnboundApply(key)
			case _: NoSuchElementException => onUnboundApply(key)
		}
	}
	def onUnboundApply(ias: TKey): ValueType = {
		IaS3.logger().warn(s"${this} does not have $ias bound")
		null.asInstanceOf[ValueType]
	}
	override def iterator = immutable.iterator
	override def toString() = getClass.getSimpleName
}
