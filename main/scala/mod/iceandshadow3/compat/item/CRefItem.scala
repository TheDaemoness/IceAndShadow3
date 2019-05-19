package mod.iceandshadow3.compat.item

import mod.iceandshadow3.util.SCaster._
import mod.iceandshadow3.compat.{CNbtTree, SRandom}
import mod.iceandshadow3.compat.entity.CRefEntity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/** Null-safe item stack + owner reference.
	*/
class CRefItem(inputstack: ItemStack, private[compat] val owner: EntityLivingBase) {
	private[compat] var is = Option(inputstack)
	def this(is: ItemStack) = this(is, null)
	//TODO: We can probably make this handle multiple item stacks.

	def isEmpty: Boolean = is.fold(true){_.getCount == 0}
	def count: Int = is.fold(0){_.getCount}
	def countMax: Int = is.fold(0){_.getMaxStackSize}
	def hasOwner: Boolean = owner != null && owner.isAlive
	def getOwner: CRefEntity = if(hasOwner) new CRefEntity(owner) else null
	def canDamage: Boolean = is.fold(false){_.isDamageable}
	def isDamaged: Boolean = is.fold(false){_.isDamaged}
	def getDamage: Int = is.fold(0){_.getDamage}
	def getDamageMax: Int = is.fold(0){_.getMaxDamage}
	def isShiny: Boolean = is.fold(false){_.hasEffect}
	def isComplex: Boolean = is.fold(false){_.hasTag}

	def exposeNbtTree(): CNbtTree = new CNbtTree(is.fold[NBTTagCompound](null){_.getTag})
	def exposeItemsOrNull(): ItemStack = is.orNull

	def changeTo(alternate: CRefItem): Unit = {is = Option(alternate.is.fold[ItemStack](null){_.copy})}
	def changeCount(newcount: Int): Unit = is.foreach(is => {if(is.isStackable) is.setCount(newcount)})

	//TODO: Enchantment querying.
	//TODO: Item frame handling.
	
	/** Reduce stack size or damage the item by the specified amount.
	 */
	def consume(count: Int = 1): Int = {
		if(this.is.isEmpty) return 0
		val is = this.is.get //Warning: shadowing.
		//TODO: There's no reason why IaS3 items can't have an override for this.
		if(is.isDamageable) {
			val rng = SRandom.getRNG(owner)
			var dmg = Math.max(0, getDamageMax - getDamage - count) //Intended: ignoring the actual durability increase.
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
			dmg
		} else {
			val size = is.getCount
			is.shrink(count)
			size - is.getCount
		}
	}
	def matches(b: Any): Boolean = if(b == null) isEmpty else b match {
		case cri: CRefItem => cri.is.fold(isEmpty){matches(_)}
		case bis: ItemStack => is.fold(false){_.isItemEqualIgnoreDurability(bis)}
		case _ => false
	}
}
object CRefItem {
	def get(inv: IInventory, index: Int, owner: EntityLivingBase = null): CRefItem = {
		val stack = if(index >= inv.getSizeInventory) null else inv.getStackInSlot(index)
		new CRefItem(stack, owner)
	}
	def make(id: String, owner: EntityLivingBase = null): CRefItem =
		new CRefItem(ItemConversions.newItemStack(id), owner)
	def make(id: String, count: Int, owner: EntityLivingBase): CRefItem = {
		val option = make(id, owner)
		option.is.foreach{_.setCount(count)}
		option
	}
	def make(id: String, count: Int): CRefItem = make(id, count, null)
}
