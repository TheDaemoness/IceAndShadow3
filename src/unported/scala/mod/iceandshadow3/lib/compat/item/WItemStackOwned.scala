package mod.iceandshadow3.lib.compat.item

import javax.annotation.Nonnull
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.world.TWWorldPlace
import mod.iceandshadow3.lib.spatial.IPositionalFine
import net.minecraft.entity.Entity
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.item.ItemStack

/** Item stack and owner reference.
	* The itemstack bit is null-safe, the owner bit is not.
	*/
class WItemStackOwned[+Owner <: WEntity](inputstack: ItemStack, @Nonnull val owner: Owner)
extends WItemStack(inputstack) with TWWorldPlace with IPositionalFine {

	override def copy: WItemStackOwned[Owner] = new WItemStackOwned(is, owner)
	final override def hasOwner = true
	override def getContainerStack: Option[WItemStackOwned[Owner]] = {
		val exposed = expose()
		if(exposed.hasContainerItem) Some(new WItemStackOwned(exposed.getContainerItem, owner)) else None
	}

	/** Reduce stack size or damage the item by the specified amount.
	 */
	def degrade(count: Int = 1): Int = {
		if(this.isEmpty) return 0
		if(owner.isCreative) return count
		val is = this.is //Warning: shadowing.
		//TODO: There's no reason why IaS3 items can't have an override for this.
		if(is.isDamageable) {
			val oldDmg = is.getDamage
			owner.damageItem(is, count)
			is.getDamage - oldDmg
		} else {
			val size = is.getCount
			is.shrink(count)
			size - is.getCount
		}
	}
	override def posFine = owner.posFine
	override protected[compat] def exposeWorld() = owner.exposeWorld()
}

