package mod.iceandshadow3.data

trait ILineSerializable {
	def fromLine(line: String): Unit
	def toLine(): String
}

trait IDataTreeSerializable {
	def fromDataTree(datum: BDataTree[_]): Unit
	def getDataTree(): BDataTree[_]
	// ^ Why the name deviation? Because this is called on both serialization and deserialization.
}