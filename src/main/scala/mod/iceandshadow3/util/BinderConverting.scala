package mod.iceandshadow3.util

import scala.reflect.ClassTag

class BinderConverting[KeyType: ClassTag, CnvType <: KeyType, ValueType <: Object: ClassTag](cnv: CnvType => ValueType)
	extends Binder[KeyType, ValueType]
{
	final def add(key: CnvType with TKey): Unit = {
		this.add(key, cnv(key))
	}
}
