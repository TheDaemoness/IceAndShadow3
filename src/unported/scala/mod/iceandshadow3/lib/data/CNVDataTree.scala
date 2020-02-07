package mod.iceandshadow3.lib.data

import scala.language.implicitConversions

object CNVDataTree {
	implicit def toDatum(value: Boolean) = new DatumBool(value)
	implicit def toDatum(value: Long) = new DatumInt64(value)
	implicit def toDatum(value: Int) = new DatumInt32(value)
	implicit def toDatum(value: Float) = new DatumFloat(value, true)
	implicit def toDatum(value: Double) = new DatumFloat(value, false)
	implicit def toDatum(value: String) = new DatumString(value)
	implicit def toDataTree(value: IDataTreeRW[_ <: DataTree[_]]) = value.exposeDataTree()
}