package mod.iceandshadow3.data

import mod.iceandshadow3.IaS3
import net.minecraft.nbt._

import scala.collection.JavaConverters._

class DataTreeMap extends BDataTreeBranch[
	java.util.Map[String, IDataTreeRW[_ <: BDataTree[_]]],
	String
](new java.util.HashMap) {
	override def fromNBT(tag: INBTBase) = {
		val compound = tag.asInstanceOf[NBTTagCompound]
		for(key <- compound.keySet.asScala) try {
			get(key).foreach(obj => {
				val tree = obj.exposeDataTree()
				tree.fromNBT(compound.getTag(key))
				obj.fromAnyDataTree(tree)
			})
		} catch {
			case e: Exception => IaS3.logger().error("NBT format mismatch: "+e.getMessage)
			//TODO: More information.
		}
		true //TODO: Report false on something blowing up.
	}
	override protected def writeNBT(map: java.util.Map[String,IDataTreeRW[_ <: BDataTree[_]]]) = {
		val retval = new NBTTagCompound()
		for((key, value) <- map.asScala) retval.setTag(key, value.exposeDataTree().toNBT())
		retval
	}
	override protected def copyFrom(map: java.util.Map[String,IDataTreeRW[_ <: BDataTree[_]]]): Unit =
		datum = new java.util.HashMap(map)
	override def get(key: String) =
		Option(datum.get(key))

	def add(key: String, value: IDataTreeRW[_ <: BDataTree[_]]) = {datum.put(key, value); this}
	override def iterator = datum.asScala.iterator
}