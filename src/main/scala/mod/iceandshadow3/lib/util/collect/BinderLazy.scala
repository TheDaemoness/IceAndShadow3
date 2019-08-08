package mod.iceandshadow3.lib.util.collect

import mod.iceandshadow3.IaS3

import scala.reflect.ClassTag

class BinderLazy[KeyType: ClassTag, CnvType <: KeyType, ValueType <: Object: ClassTag](cnv: CnvType => ValueType)
	extends Binder[KeyType, ValueType]
{
	type ConvertType = CnvType
	var later = new scala.collection.mutable.ListBuffer[CnvType with TKey]
	final def add(key: CnvType with TKey): Unit = {
		if(later == null) {
			IaS3.bug(key, s"lazy add() called after $this was frozen")
			return
		}
		later += key
	}

	override def freeze() = {
		for(now <- later) this.add(now, cnv(now))
		later = null
		super.freeze()
	}
}
