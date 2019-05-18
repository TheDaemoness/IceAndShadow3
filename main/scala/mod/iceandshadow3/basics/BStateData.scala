package mod.iceandshadow3.basics;

import mod.iceandshadow3.data._

abstract class BStateData extends IDataTreeSerializable[DataTreeMap] {
	val needsWrite = true
	private val dataTree = new DataTreeMap
	def fromDataTree(tree: DataTreeMap): Boolean = tree == dataTree
 	def getDataTree(): DataTreeMap = dataTree
	def register(key: String, field: IDataTreeSerializable[_ <: BDataTree[_]]): Unit = {
		dataTree.add(key, field)
	}
}
