package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.BLogicItem
import mod.iceandshadow3.lib.compat.entity.state.{AttributeModTotal, EquipPointVanilla, WAttribute}
import mod.iceandshadow3.lib.compat.entity.{CNVEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import mod.iceandshadow3.lib.compat.nbt.TNbtSource
import mod.iceandshadow3.lib.compat.util.{GlobalRandom, TLocalized, TWLogical}
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.{PlayerEntity, ServerPlayerEntity}
import net.minecraft.inventory.IInventory
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.IItemProvider
import net.minecraftforge.common.ForgeHooks

/** Null-safe item stack + owner reference.
	*/
class WItemStack(inputstack: ItemStack, private[compat] var owner: LivingEntity)
	extends BWItem
	with TWLogical[BLogicItem]
	with TLocalized
	with TNbtSource
{
	private[compat] var is = Option(inputstack)
	def this() = this(null.asInstanceOf[ItemStack], null.asInstanceOf[LivingEntity])
	def this(is: Null, owner: LivingEntity) = this(null.asInstanceOf[ItemStack], owner)
	def this(is: IItemProvider, owner: LivingEntity) = this(new ItemStack(is), owner)

	def copy: WItemStack = new WItemStack(is.fold[ItemStack](null){_.copy()}, owner)
	override def isEmpty: Boolean = is.fold(true){_.getCount == 0}
	def count: Int = is.fold(0){_.getCount}
	def countMax: Int = is.fold(0){_.getMaxStackSize}
	def hasOwner: Boolean = owner != null
	def getOwner: WEntityLiving = if(hasOwner) CNVEntity.wrap(owner) else null
	def getDamage: Int = asItemStack().getDamage
	def getDamageMax: Int = asItemStack().getMaxDamage
	def container: Option[WItemStack] = {
		val exposed = asItemStack()
		if(exposed.hasContainerItem) Some(new WItemStack(exposed.getContainerItem, owner)) else None
	}

	def isComplex: Boolean = is.fold(false){_.hasTag}

	/* TODO: Narrow scope. */ def asItemStack(): ItemStack = is.getOrElse(ItemStack.EMPTY)

	protected[item] def move(): ItemStack = {
		val retval = asItemStack().copy()
		destroy()
		retval
	}

	/** Changes the item stack referenced by this WItemStack to a copy of a different item stack.
		* IMPORTANT: This does NOT change the item internally!
		*/
	def referenceCopyOf(alternate: WItemStack): WItemStack =
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
			import mod.iceandshadow3.lib.util.Casting._
			val dmg = Math.max(0, getDamageMax - getDamage - count) //Intended: ignoring the actual durability increase.
			val multiplayer = cast[ServerPlayerEntity](owner).orNull
			if (is.attemptDamageItem(count, GlobalRandom.getRNG(owner), multiplayer)) {
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
		is.fold[CompoundNBT](null){_.getOrCreateTag()}

	def getBurnTicks: Int = is.fold(0)(ForgeHooks.getBurnTime)

	override protected[compat] def getLocalizedName = asItemStack().getTextComponent

	override def asItem() = asItemStack().getItem
	override def asWItem(): WItemType = WItemType(asItem())

	override protected[compat] def exposeNbt() = asItemStack().getOrCreateTag()
	override protected[compat] def setNbt(what: CompoundNBT): Unit = asItemStack().setTag(what)

	def apply(attr: WAttribute[_], where: EquipPointVanilla): AttributeModTotal =
		AttributeModTotal(asItemStack().getAttributeModifiers(where.where).get(attr.name))
}
object WItemStack {
	def empty = new WItemStack(null, null)
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
