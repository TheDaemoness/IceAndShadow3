package mod.iceandshadow3.data

class DatumInt64(int: Long) extends DatumInt(int) {
	override def getMin = Long.MinValue
	override def getMax = Long.MaxValue
}
class DatumInt32(int: Int) extends DatumInt(int) {
	override def getMin = Int.MinValue
	override def getMax = Int.MaxValue
}
class DatumInt16(int: Short) extends DatumInt(int) {
	override def getMin = Short.MinValue
	override def getMax = Short.MaxValue
}
class DatumInt8(int: Byte) extends DatumInt(int) {
	override def getMin = Byte.MinValue
	override def getMax = Byte.MaxValue
}
class DatumInt64Array(size: Int) extends DatumIntArray(size) {
	override def getMin = Long.MinValue
	override def getMax = Long.MaxValue
}
class DatumInt32Array(size: Int) extends DatumIntArray(size) {
	override def getMin = Int.MinValue
	override def getMax = Int.MaxValue
}
class DatumInt8Array(size: Int) extends DatumIntArray(size) {
	override def getMin = Byte.MinValue
	override def getMax = Byte.MaxValue
}
//TODO: Variants for enumerations.