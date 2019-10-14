package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.IaS3

import scala.reflect.ClassTag

class Binder[KeyType: ClassTag, ValueType <: Object: ClassTag] extends Iterable[ValueType] {
	private var mutable = new scala.collection.mutable.ListBuffer[ValueType]
	private var immutable: Array[ValueType] = Array.empty
	trait TKey {
		this: KeyType =>
		private[Binder] var binderIndex: Int = -1
	}
	def frozen = mutable == null
	def freeze(): Array[ValueType] = {
		immutable = mutable.toArray[ValueType]
		mutable = null
		immutable
	}
	final def add(ias: TKey, adapter: ValueType): Unit = {
		if(mutable == null) {
			IaS3.bug(ias, s"add called after $this was frozen")
			return
		}
		ias.binderIndex = mutable.size
		mutable += adapter
	}
	def apply(ias: TKey): ValueType = {
		if(immutable == null) return onUnboundApply(ias)
		val index = ias.binderIndex
		if(index >= 0) immutable(index)
		else onUnboundApply(ias)
	}
	def onUnboundApply(ias: TKey): ValueType = {
		IaS3.logger().warn(s"${this} does not have $ias bound")
		null.asInstanceOf[ValueType]
	}
	override def iterator = immutable.iterator
}
