package mod.iceandshadow3.util

import scala.reflect.ClassTag

class BinderConverting[KeyType: ClassTag, ValueType <: Object: ClassTag](cnv: KeyType => ValueType)
	extends Binder[KeyType, ValueType]
{
	final def add(key: KeyType with TKey): Unit = {
		this.add(key, cnv(key))
	}
}
