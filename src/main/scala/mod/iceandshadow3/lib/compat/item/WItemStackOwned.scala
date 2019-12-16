package mod.iceandshadow3.lib.compat.item

import javax.annotation.Nonnull
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.util.GlobalRandom
import mod.iceandshadow3.lib.compat.world.TWWorldPlace
import mod.iceandshadow3.lib.spatial.IPositionalFine
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.item.ItemStack

/** Item stack and owner reference.
	* The itemstack bit is null-safe, the owner bit is not.
	*/
class WItemStackOwned[+Owner <: WEntity](inputstack: ItemStack, @Nonnull val owner: Owner)
extends WItemStack(inputstack) with TWWorldPlace with IPositionalFine {

	override def copy: WItemStackOwned[Owner] = new WItemStackOwned(is.orNull, owner)
	final override def hasOwner = true
	override def container: Option[WItemStackOwned[Owner]] = {
		val exposed = asItemStack()
		if(exposed.hasContainerItem) Some(new WItemStackOwned(exposed.getContainerItem, owner)) else None
	}

	/** Reduce stack size or damage the item by the specified amount.
	 */
	def degrade(count: Int = 1): Int = {
		if(this.isEmpty) return 0
		if(owner.isCreative) return count
		val is = this.is.get //Warning: shadowing.
		//TODO: There's no reason why IaS3 items can't have an override for this.
		if(is.isDamageable) {
			import mod.iceandshadow3.lib.util.Casting._
			val dmg = Math.max(0, getDamageMax - getDamage - count) //Intended: ignoring the actual durability increase.
			val multiplayer = cast[ServerPlayerEntity](owner).orNull
			if (is.attemptDamageItem(count, GlobalRandom.getRNG(owner.entity), multiplayer)) {
				if(owner != null) {
					//TODO: Break animation.
					//Research: Is there a point of the orElse below?
					Option(multiplayer).orElse(cast[PlayerEntity](owner)).foreach {
						_.addStat(net.minecraft.stats.Stats.ITEM_BROKEN.get(is.getItem))
					}
				}
				is.shrink(1)
				is.setDamage(0)
			}
			dmg
		} else {
			val size = is.getCount
			is.shrink(count)
			size - is.getCount
		}
	}
	override def posFine = owner.posFine
	override protected[compat] def exposeWorld() = owner.exposeWorld()
}

