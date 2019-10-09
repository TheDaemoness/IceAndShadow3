package mod.iceandshadow3.lib.compat.misc

import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

class ItemMap[T] extends ResourceMap[T] {
	override protected[compat] def canAdd(what: ResourceLocation) = {
		ForgeRegistries.ITEMS.containsKey(what)
	}
}
