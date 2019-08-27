package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.IaS3

import scala.reflect.ClassTag

class BinderEarly[KeyType: ClassTag, ValueType <: Object: ClassTag]
	extends Binder[KeyType, ValueType]
{
	var early = new scala.collection.mutable.HashMap[TKey, ValueType]
	final def add(key: KeyType with TKey, value: ValueType): Unit = {
		if(early == null) {
			IaS3.bug(key, s"early add() called after $this was frozen")
			return
		}
		early.put(key, value)
	}

	override def freeze() = {
		for(now <- early) this.add(now._1, now._2)
		early = null
		super.freeze()
	}

	override def apply(ias: TKey) = {
		if(early != null) early.getOrElse(ias, {
			IaS3.bug(ias, s"Unbound key provided to $this.apply")
			null.asInstanceOf[ValueType]
		})
		else super.apply(ias)
	}
}
