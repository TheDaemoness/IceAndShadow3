package mod.iceandshadow3.compat.text

import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

object TextUtils {
	def itemIdToUnlocalized(id: String): String = {
		val item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id))
		if(item == null) "" else item.getTranslationKey
	}
}
