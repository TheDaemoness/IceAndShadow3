package mod.iceandshadow3.data

trait IDataTreeRW[DataTreeType <: BDataTree[_]] {
	def exposeDataTree(): DataTreeType
	def fromDataTree(tree: DataTreeType): Boolean
	def fromAnyDataTree(tree: BDataTree[_]): Boolean = fromDataTree(tree.asInstanceOf[DataTreeType])
}