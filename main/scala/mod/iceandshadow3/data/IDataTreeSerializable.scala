package mod.iceandshadow3.data

trait IDataTreeSerializable[DataTreeType <: BDataTree[_]] {
	def getDataTree(): DataTreeType
	def fromDataTree(tree: DataTreeType): Boolean
	def fromAnyDataTree(tree: BDataTree[_]): Boolean = fromDataTree(tree.asInstanceOf[DataTreeType])
}