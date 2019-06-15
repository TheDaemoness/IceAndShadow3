package mod.iceandshadow3.compat.misc

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.util.StringMap
import net.minecraft.util.ResourceLocation

class ResourceMap[T] extends scala.collection.mutable.Map[String, T] {
	val realmap = new StringMap[T]
	override def +=(kv: (String, T)) = {
		val resourceloc = new ResourceLocation(kv._1)
		if(resourceloc.getNamespace.contentEquals(IaS3.MODID)) {
			IaS3.logger().warn(s"Attempted to illegally add an IaS3 resource ($resourceloc) to $this")
		} else if(canAdd(resourceloc)) {
			realmap.put(kv._1, kv._2)
		} else {
			IaS3.logger().warn(s"Cannot add ($resourceloc) to $this")
		}
		this
	}
	def canAdd(what: ResourceLocation) = true
	override def -=(key: String) = {realmap.remove(key); this}
	override def get(key: String) = Option(realmap.get(key))
	override def iterator = realmap.iterator
}
