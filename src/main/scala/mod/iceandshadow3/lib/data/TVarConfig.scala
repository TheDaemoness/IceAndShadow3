package mod.iceandshadow3.lib.data

trait TVarConfig[T] {
	this: BVar[T] =>
	def readLine(what: String): T
	def toLine(value: T): String
}
