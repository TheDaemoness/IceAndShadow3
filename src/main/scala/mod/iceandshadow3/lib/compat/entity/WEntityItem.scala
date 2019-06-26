package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.util.IWrapper
import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemStack

class WEntityItem protected[entity](protected[compat] val entityitem: ItemEntity) extends WEntity(entityitem)
	with IWrapper[ItemStack]
{
	def ttl = entityitem.lifespan
	override protected[compat] def expose() = entityitem.getItem
	def item = new WItemStack(expose(), null)
	def setItem(what: WItemStack): Unit = entityitem.setItem(what.exposeItems())
	def enablePickup(): Unit = entityitem.setNoPickupDelay()
	def canPickup: Boolean = !entityitem.cannotPickup()
	def immortalize(): Unit = entityitem.setNoDespawn()

	//Wait, ItemEntities actually contain the thrower's UUID? Might be useful.
}
