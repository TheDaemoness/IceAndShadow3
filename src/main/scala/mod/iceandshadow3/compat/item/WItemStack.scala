package mod.iceandshadow3.compat.item

import mod.iceandshadow3.basics.BLogicItem
import mod.iceandshadow3.basics.util.LogicPair
import mod.iceandshadow3.compat.entity.{CNVEntity, WEntity, WEntityLiving}
import mod.iceandshadow3.compat.{TWLogical, ILogicItemProvider, SRandom}
import mod.iceandshadow3.util.Casting._
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.inventory.IInventory
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.IItemProvider

/** Null-safe item stack + owner reference.
	*/
class WItemStack(inputstack: ItemStack, private[compat] var owner: EntityLivingBase)
	extends TWLogical[BLogicItem]
	with ILogicItemProvider
{
	private[compat] var is = Option(inputstack)
	def this() = this(null.asInstanceOf[ItemStack], null.asInstanceOf[EntityLivingBase])
	def this(is: Null, owner: EntityLivingBase) = this(null.asInstanceOf[ItemStack], owner)
	def this(is: IItemProvider, owner: EntityLivingBase) = this(new ItemStack(is), owner)
	//TODO: We can probably make this handle multiple item stacks.

	def copy: WItemStack = new WItemStack(is.fold[ItemStack](null){_.copy()}, owner)
	def isEmpty: Boolean = is.fold(true){_.getCount == 0}
	def count: Int = is.fold(0){_.getCount}
	def countMax: Int = is.fold(0){_.getMaxStackSize}
	def hasOwner: Boolean = owner != null
	def getOwner: WEntityLiving = if(hasOwner) CNVEntity.wrap(owner) else null
	def canDamage: Boolean = is.fold(false){_.isDamageable}
	def isDamaged: Boolean = is.fold(false){_.isDamaged}
	def getDamage: Int = is.fold(0){_.getDamage}
	def getDamageMax: Int = is.fold(0){_.getMaxDamage}
	def isShiny: Boolean = is.fold(false){_.hasEffect}
	def isComplex: Boolean = is.fold(false){_.hasTag}

	def exposeItems(): ItemStack = is.getOrElse(ItemStack.EMPTY)
	def exposeItemsOrNull(): ItemStack = is.orNull

	protected[item] def move(): ItemStack = {
		val retval = exposeItems().copy()
		destroy()
		retval
	}

	def changeTo(alternate: WItemStack): WItemStack =
		{is = Option(alternate.is.fold[ItemStack](null){_.copy}); this}
	def changeCount(newcount: Int): WItemStack =
		{is.foreach(is => {if(is.isStackable) is.setCount(Math.min(countMax,newcount))}); this}
	def changeOwner(who: WEntityLiving): WItemStack =
		{owner = who.living; this}

	//TODO: Enchantment querying.
	//TODO: Item frame handling.
	
	/** Reduce stack size or damage the item by the specified amount.
	 */
	def consume(count: Int = 1): Int = {
		if(this.isEmpty) return 0
		if(owner != null && CNVEntity.wrap(owner).isCreative) return count
		val is = this.is.get //Warning: shadowing.
		//TODO: There's no reason why IaS3 items can't have an override for this.
		if(is.isDamageable) {
			val dmg = Math.max(0, getDamageMax - getDamage - count) //Intended: ignoring the actual durability increase.
			val multiplayer = cast[EntityPlayerMP](owner).orNull
			if (is.attemptDamageItem(count, SRandom.getRNG(owner), multiplayer)) {
				if(owner != null) {
					owner.renderBrokenItemStack(is)
					//Research: Is there a point of the orElse below?
					Option(multiplayer).orElse(cast[EntityPlayer](owner)).foreach {
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
	def release(): Unit = {
		is = None
	}
	def destroy(): Unit = {
		is.foreach(_.setCount(0))
		release()
	}

	def matches(b: Any): Boolean = if(b == null) isEmpty else b match {
		case cri: WItemStack => cri.is.fold(isEmpty){matches(_)}
		case bis: ItemStack => is.fold(false){_.isItemEqualIgnoreDurability(bis)}
		case _ => false
	}

	override protected def exposeCompoundOrNull() =
		is.fold[NBTTagCompound](null){_.getOrCreateTag()}

	override def getLogicPair: LogicPair[BLogicItem] =
		is.fold[LogicPair[BLogicItem]](null){_.getItem match {
			case lp: ILogicItemProvider => return lp.getLogicPair
			case _ => return null
		}}
}
object WItemStack {
	def get(inv: IInventory, index: Int): WItemStack = {
		val stack = if(index >= inv.getSizeInventory) null else inv.getStackInSlot(index)
		new WItemStack(stack, null)
	}
	def make(id: String): WItemStack =
		new WItemStack(CNVItem.newItemStack(id), null)
	def make(logic: BLogicItem, variant: Int): WItemStack = {
		val item: Item = BinderItem(logic)(variant).asInstanceOf[Item]
		if(item == null) new WItemStack() else new WItemStack(item, null)
	}
}
