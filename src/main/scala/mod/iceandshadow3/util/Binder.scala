package mod.iceandshadow3.util

import mod.iceandshadow3.IaS3

import scala.reflect.ClassTag

class Binder[KeyType: ClassTag, ValueType <: Object: ClassTag] {
	var mutable = new scala.collection.mutable.ListBuffer[ValueType]
	var immutable: Array[ValueType] = _
	trait TKey {
		this: KeyType =>
		private[Binder] var binderIndex: Int = -1
	}
	def freeze(): Array[ValueType] = {
		immutable = mutable.toArray[ValueType]
		mutable = null
		immutable
	}
	final def add(ias: TKey, adapter: ValueType): Unit = {
		if(mutable == null) {
			IaS3.bug(ias, "update() called after binder was frozen")
			return
		}
		ias.binderIndex = mutable.size
		mutable += adapter
	}
	final def apply(ias: TKey): ValueType = {
		if(immutable == null) {
			IaS3.bug(ias, "apply() called before binder was frozen")
			return null.asInstanceOf[ValueType]
		}
		val index = ias.binderIndex
		if(index >= 0) immutable(index)
		else {
			IaS3.bug(ias, "Unbound key provided to apply()")
			null.asInstanceOf[ValueType]
		}
	}
}
