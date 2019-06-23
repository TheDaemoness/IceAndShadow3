package mod.iceandshadow3.compat.misc

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.util.StringMap
import net.minecraft.util.ResourceLocation

/** Resource location map for NON-IAS resources.
	*/
class ResourceMap[T] extends StringMap[T] {
	override def isValidKey(key: String) = {
		val resourceloc = new ResourceLocation(key)
		if(resourceloc.getNamespace.contentEquals(IaS3.MODID)) {
			IaS3.bug(null,"Cannot add iceandshadow3 resources to ResourceMaps")
			false
		} else canAdd(resourceloc)
	}

	def canAdd(what: ResourceLocation) = true
}
