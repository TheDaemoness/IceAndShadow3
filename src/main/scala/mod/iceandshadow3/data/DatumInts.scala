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
//TODO: The following have checks that are superfluous (unless these classes are extended).
class DatumInt64Array(size: Int) extends DatumIntArray(size) {
	def this(what: Array[Long]) = {
		this(what.length)
		set(what)
	}
	override def getMin = Long.MinValue
	override def getMax = Long.MaxValue
}
class DatumInt32Array(size: Int) extends DatumIntArray(size) {
	def this(what: Array[Int]) = {
		this(what.length)
		set(what)
	}
	override def getMin = Int.MinValue
	override def getMax = Int.MaxValue
}
class DatumInt8Array(size: Int) extends DatumIntArray(size) {
	def this(what: Array[Byte]) = {
		this(what.length)
		set(what)
	}
	override def getMin = Byte.MinValue
	override def getMax = Byte.MaxValue
}
//TODO: Variants for enumerations.