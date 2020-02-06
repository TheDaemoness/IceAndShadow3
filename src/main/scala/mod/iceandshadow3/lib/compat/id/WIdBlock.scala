package mod.iceandshadow3.lib.compat.id

import mod.iceandshadow3.lib.base.TNamed
import mod.iceandshadow3.lib.compat.block.WBlockType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.ForgeRegistries

import scala.language.implicitConversions

final class WIdBlock(vanilla: ResourceLocation) extends WRegistryId[WBlockType](vanilla) with TNamed[WIdBlock] {
	override protected def translationKeyPrefix = "block"
	def this(namespace: String, fullname: String) = this(new ResourceLocation(namespace, fullname))
	override def id = this
	override def get = {
		val block = ForgeRegistries.BLOCKS.getValue(asVanilla)
		if(block == null) None else Some[WBlockType](() => block)
	}
}
object WIdBlock {
	implicit def apply(fullname: String): WIdBlock = new WIdBlock(new ResourceLocation(fullname))
}
