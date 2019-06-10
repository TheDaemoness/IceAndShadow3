package mod.iceandshadow3.data

import mod.iceandshadow3.IaS3
import net.minecraft.nbt._

import scala.collection.JavaConverters._

class DataTreeList[Element <: IDataTreeRW[_ <: BDataTree[_]]] extends BDataTreeBranch[java.util.List[Element], Int](new java.util.ArrayList) {
	override def fromNBT(tag: INBT): Boolean = {
		val list = tag.asInstanceOf[ListNBT]
		for (i <- 0 until list.size) try {
			val element = getForRead(i)
			element.foreach {_.exposeDataTree().fromNBT(list.get(i))}
		} catch {
			case e: Exception => IaS3.logger().error("Barely-handled NBT format mismatch: "+e.getMessage)
			return false
		}
		true
	}
	override protected def writeNBT(list: java.util.List[Element]) = {
		val retval = new ListNBT()
		for(elem <- list.asScala) retval.add(elem.exposeDataTree().toNBT())
		retval
	}
	override protected def copyFrom(list: java.util.List[Element]): Unit =
		datum = new java.util.ArrayList(list)
	override def get(key: Int) = if(key < datum.size()) Some(datum.get(key)) else None
	
	override def iterator = ((0 until datum.size) zip datum.asScala).iterator

	//TODO: Allow IDataTreeSerializable
	//TODO: Add some way of actually adding stuff to this class.
}
