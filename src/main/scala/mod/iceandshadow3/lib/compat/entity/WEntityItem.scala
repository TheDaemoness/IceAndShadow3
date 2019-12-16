package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import net.minecraft.entity.item.ItemEntity

class WEntityItem protected[entity](protected[compat] val entityitem: ItemEntity) extends WEntity(entityitem)
{
	def ttl = entityitem.lifespan
	protected[compat] def expose() = entityitem.getItem
	def item = new WItemStackOwned(expose(), this)
	def setItem(what: WItemStack): Unit = entityitem.setItem(what.asItemStack())
	def enablePickup(): Unit = entityitem.setNoPickupDelay()
	def canPickup: Boolean = !entityitem.cannotPickup()
	def immortalize(): Unit = entityitem.setNoDespawn()

	//Wait, ItemEntities actually contain the thrower's UUID? Might be useful.
}
