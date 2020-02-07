package mod.iceandshadow3.lib.data

import mod.iceandshadow3.IaS3
import net.minecraft.nbt._

import scala.jdk.CollectionConverters._

class DataTreeMap extends DataTreeBranch[
	java.util.Map[String, IDataTreeRW[_ <: DataTree[_]]],
	String
](new java.util.HashMap) {

	def this(existing: Iterable[(String, IDataTreeRW[_ <: DataTree[_]])]) = {
		this()
		for((key, value) <- existing) add(key, value)
	}
	override def iterator = datum.asScala.iterator

	override def fromNBT(tag: INBT) = {
		var greatSuccess: Boolean = true
		val compound = tag.asInstanceOf[CompoundNBT]
		for(key <- compound.keySet.asScala) try {
			val option = getForRead(key)
			if(option.isEmpty) IaS3.logger().warn(s"Unknown key $key found in NBT compound $compound.")
			option.foreach(obj => {
				val tree = obj.exposeDataTree()
				val objname = obj.getClass.getSimpleName
				if(!tree.fromNBT(compound.get(key))) {
					greatSuccess = false
					IaS3.logger().warn(s"An $objname with key $key failed to deserialize from NBT compound $compound.")
				}
				if(!obj.fromAnyDataTree(tree)) {
					greatSuccess = false
					IaS3.bug(obj, s"An $objname with key $key failed to deserialize from its own data tree.")
				}
			})
		} catch {
			case e: Exception => IaS3.logger().error("NBT format mismatch: "+e.getMessage)
			greatSuccess = false;
		}
		greatSuccess
	}
	override protected def writeNBT(map: java.util.Map[String,IDataTreeRW[_ <: DataTree[_]]]) = {
		val retval = new CompoundNBT()
		for((key, value) <- map.asScala) retval.put(key, value.exposeDataTree().toNBT)
		retval
	}

	override protected def copyFrom(map: java.util.Map[String,IDataTreeRW[_ <: DataTree[_]]]): Unit =
		datum = new java.util.HashMap(map)

	override def get(key: String) =
		Option(datum.get(key))

	def add(key: String, value: IDataTreeRW[_ <: DataTree[_]]) = {
		if(value == null) this
		else {datum.put(key, value); this}
	}
}