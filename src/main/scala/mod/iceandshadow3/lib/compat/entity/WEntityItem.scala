package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import net.minecraft.entity.item.ItemEntity

class WEntityItem protected[entity](override protected[compat] val expose: ItemEntity) extends WEntity(expose)
{
	def ttl = expose.lifespan
	protected[compat] def exposeStack() = expose.getItem
	def item = new WItemStackOwned(exposeStack(), this)
	def setItem(what: WItemStack): Unit = expose.setItem(what.expose())
	def enablePickup(): Unit = expose.setNoPickupDelay()
	def canPickup: Boolean = !expose.cannotPickup()
	def immortalize(): Unit = expose.setNoDespawn()

	//Wait, ItemEntities actually contain the thrower's UUID? Might be useful.
}
