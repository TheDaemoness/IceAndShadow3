package mod.iceandshadow3.basics

import mod.iceandshadow3.data._

/** Class for storing data that normally gets loaded from/written to NBT.
	*/
abstract class BStateData extends IDataTreeRW[DataTreeMap] {
	val needsWrite = true
	private val dataTree = new DataTreeMap
	def fromDataTree(tree: DataTreeMap): Boolean = tree == dataTree
 	def exposeDataTree(): DataTreeMap = dataTree
	def register(key: String, field: IDataTreeRW[_ <: BDataTree[_]]): Unit = {
		dataTree.add(key, field)
	}
}
