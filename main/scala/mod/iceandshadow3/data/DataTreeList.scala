package mod.iceandshadow3.data

import mod.iceandshadow3.IaS3
import net.minecraft.nbt._

import scala.collection.JavaConverters._

class DataTreeList[Element <: BDataTree[_]] extends BDataTreeBranch[java.util.List[Element], Int](new java.util.ArrayList) {
	override def fromNBT(tag: INBTBase) = {
		val list = tag.asInstanceOf[NBTTagList]
		for ((elem, i) <- datum.asScala zip (0 until list.size)) try {
			elem.fromNBT(list.get(i))
		} catch {
			case e: Exception => IaS3.logger().error("NBT format mismatch: "+e.getMessage)
			//TODO: More information?
		}
		true //TODO: Report false on something blowing up.
	}
	override protected def writeNBT(list: java.util.List[Element]) = {
		val retval = new NBTTagList()
		for(elem <- list.asScala) retval.add(elem.toNBT())
		retval
	}
	override protected def copyFrom(list: java.util.List[Element]): Unit =
		datum = new java.util.ArrayList(list)
	override def get(key: Int) = if(key < datum.size()) Some(datum.get(key)) else None
	
	override def iterator = ((0 until datum.size) zip datum.asScala).iterator

	//TODO: Allow IDataTreeSerializable
	//TODO: Add some way of actually adding stuff to this class.
}
