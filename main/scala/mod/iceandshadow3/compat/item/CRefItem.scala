package mod.iceandshadow3.compat.item

import mod.iceandshadow3.util.SCaster._
import mod.iceandshadow3.compat.SRandom
import mod.iceandshadow3.compat.entity.CRefEntity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer,EntityPlayerMP}
import net.minecraft.item.ItemStack

//TODO: Manually generated class stub.
class CRefItem(private[compat] var is: ItemStack, private[compat] val owner: EntityLivingBase) {
	def this(is: ItemStack) = this(is, null)
	
	def count(): Int = is.getCount
	def countMax(): Int = is.getMaxStackSize
	def hasOwner = owner != null
	def getOwner: CRefEntity = if(hasOwner) new CRefEntity(owner) else null
	
	/** Reduce stack size or damage the item by the specified amount.
	 */
	def consume(count: Int = 1): Unit = {
		if(is.isDamageable) {
			val rng = SRandom.getRNG(owner)
			if (is.attemptDamageItem(count, rng, cast[EntityPlayerMP](owner).orNull)) {
				if(owner != null) {
					owner.renderBrokenItemStack(is)
					cast[EntityPlayer](owner).foreach {
						_.addStat(net.minecraft.stats.StatList.ITEM_BROKEN.get(is.getItem))
					}
				}
				is.shrink(1)
				is.setDamage(0)
			}
		}
		else is.shrink(count)
		//TODO: There's no reason why IaS3 items can't have an override for this.
	}
}
