package mod.iceandshadow3.lib.compat.misc

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.util.collect.StringMap
import net.minecraft.util.ResourceLocation

/** Resource location map for NON-IAS resources.
	*/
class ResourceMap[T] extends StringMap[T] {
	override def isValidKey(key: String) = {
		val resourceloc = new ResourceLocation(key)
		if(resourceloc.getNamespace.contentEquals(IaS3.MODID)) {
			IaS3.logger().warn(s"Cannot add $resourceloc to $this")
			false
		} else canAdd(resourceloc)
	}

	protected[compat] def canAdd(what: ResourceLocation) = true
}
