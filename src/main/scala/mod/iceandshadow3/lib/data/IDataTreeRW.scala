package mod.iceandshadow3.lib.data

trait IDataTreeRW[DataTreeType <: DataTree[_]] {
	def exposeDataTree(): DataTreeType
	def fromDataTree(tree: DataTreeType): Boolean
	private[data] def fromAnyDataTree(tree: DataTree[_]): Boolean = fromDataTree(tree.asInstanceOf[DataTreeType])
}