package mod.iceandshadow3.compat.misc

import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

class ItemMap[T] extends ResourceMap[T] {
	override def canAdd(what: ResourceLocation) = {
		ForgeRegistries.ITEMS.containsKey(what)
	}
}
