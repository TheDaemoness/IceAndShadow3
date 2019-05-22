package mod.iceandshadow3.compat.item

import mod.iceandshadow3.IaS3
import net.minecraft.item.ItemStack
import net.minecraft.util.{ResourceLocation, ResourceLocationException}
import net.minecraftforge.registries.ForgeRegistries

object ItemConversions {
  private def invalidIDBug(message: String): Unit =
    IaS3.bug(new IllegalArgumentException(), "newItemStack called with invalid ID \"$name\": $message")

  implicit private[item] def newItemStack(name: String): ItemStack = {
    try {
      val newstack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(name)))
      if (newstack != null) return newstack
      else invalidIDBug("Item does not exist")
    } catch {
      case e: ResourceLocationException => invalidIDBug(e.getMessage)
    }
    null
  }
}
