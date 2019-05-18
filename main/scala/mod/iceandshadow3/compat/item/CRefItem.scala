package mod.iceandshadow3.compat.item

import mod.iceandshadow3.util.SCaster._
import mod.iceandshadow3.compat.SRandom
import mod.iceandshadow3.compat.entity.CRefEntity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack

//TODO: Manually generated class stub.
class CRefItem(private[compat] var is: ItemStack, private[compat] val owner: Entity) {
	def this(is: ItemStack) = this(is, null)
	
	def count(): Int = is.getCount();
	def countMax(): Int = is.getMaxStackSize();
	def getOwner() = if(owner != null) Some(new CRefEntity(owner)) else None
	
	/** Reduce stack size or damage the item by the specified amount.
	 */
	def consume(count: Int = 1) = {
		if(is.isDamageable) {
			val elb = cast[EntityLivingBase](owner).getOrElse(null)
			val rng = SRandom.getRNG(elb)
			if (is.attemptDamageItem(count, rng, cast[EntityPlayerMP](elb).getOrElse(null))) {
				if(elb != null) elb.renderBrokenItemStack(is)
				cast[EntityPlayer](elb).foreach{_.addStat(net.minecraft.stats.StatList.ITEM_BROKEN.get(is.getItem()))}
				is.shrink(1)
				is.setDamage(0)
			}
		}
		else is.shrink(count)
		//TODO: There's no reason why IaS3 items can't have an override for this.
	}
}
