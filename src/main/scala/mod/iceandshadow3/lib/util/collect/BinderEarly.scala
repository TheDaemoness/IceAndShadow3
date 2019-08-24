package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.IaS3

import scala.reflect.ClassTag

class BinderEarly[KeyType: ClassTag, CnvType <: KeyType, ValueType <: Object: ClassTag](cnv: CnvType => ValueType)
	extends Binder[KeyType, ValueType]
{
	type ConvertType = CnvType
	var early = new scala.collection.mutable.HashMap[TKey, ValueType]
	final def add(key: CnvType with TKey): Unit = {
		if(early == null) {
			IaS3.bug(key, s"early add() called after $this was frozen")
			return
		}
		early.put(key, cnv(key))
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
