package mod.iceandshadow3.data

import mod.iceandshadow3.IceAndShadow3
import net.minecraft.nbt._
import scala.collection.JavaConverters._

class DataTreeMap extends BDataTreeBranch[
	java.util.Map[String, IDataTreeSerializable[_ <: BDataTree[_]]],
	String
](new java.util.HashMap) {
	override def fromNBT(tag: INBTBase) = {
		val compound = tag.asInstanceOf[NBTTagCompound]
		for(key <- compound.keySet.asScala) try {
			get(key).foreach(obj => {
				val tree = obj.getDataTree()
				tree.fromNBT(compound.getTag(key))
				obj.fromAnyDataTree(tree)
			})
		} catch {
			case e: Exception => IceAndShadow3.logger().error("NBT format mismatch: "+e.getMessage)
			//TODO: More information.
		}
		true //TODO: Report false on something blowing up.
	}
	override protected def writeNBT(map: java.util.Map[String,IDataTreeSerializable[_ <: BDataTree[_]]]) = {
		val retval = new NBTTagCompound()
		for((key, value) <- map.asScala) retval.setTag(key, value.getDataTree().toNBT())
		retval
	}
	override protected def copyFrom(map: java.util.Map[String,IDataTreeSerializable[_ <: BDataTree[_]]]) =
		datum = new java.util.HashMap(map)
	override def get(key: String) =
		mod.iceandshadow3.util.SCaster.option(datum.get(key))

	def add(key: String, value: IDataTreeSerializable[_ <: BDataTree[_]]) = {datum.put(key, value); this}
	override def iterator = datum.asScala.iterator
}