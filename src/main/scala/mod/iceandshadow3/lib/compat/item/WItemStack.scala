package mod.iceandshadow3.lib.compat.item

import mod.iceandshadow3.lib.compat.block.impl.BinderBlock
import mod.iceandshadow3.lib.{BLogicBlock, BLogicItem}
import mod.iceandshadow3.lib.compat.entity.state.{AttributeModTotal, EquipPointVanilla, WAttribute}
import mod.iceandshadow3.lib.compat.entity.WEntity
import mod.iceandshadow3.lib.compat.item.impl.BinderItem
import mod.iceandshadow3.lib.compat.nbt.TNbtSource
import mod.iceandshadow3.lib.compat.util.{TLocalized, TWLogical}
import net.minecraft.inventory.IInventory
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.IItemProvider
import net.minecraftforge.common.ForgeHooks

/** Null-safe item stack.
	*/
class WItemStack(inputstack: ItemStack)
	extends BWItem
	with TWLogical[BLogicItem]
	with TLocalized
	with TNbtSource
{
	protected[compat] var is = Option(inputstack)
	def this() = this(null.asInstanceOf[ItemStack])
	def this(is: IItemProvider) = this(new ItemStack(is))

	def copy: WItemStack = new WItemStack(is.fold[ItemStack](null){_.copy()})
	override def isEmpty: Boolean = is.fold(true){_.getCount == 0}
	def count: Int = is.fold(0){_.getCount}
	def countMax: Int = is.fold(0){_.getMaxStackSize}
	def hasOwner: Boolean = false
	def getDamage: Int = asItemStack().getDamage
	def getDamageMax: Int = asItemStack().getMaxDamage
	def setDamage(amount: Int): this.type = {
		asItemStack().setDamage(amount)
		this
	}
	def container: Option[WItemStack] = {
		val exposed = asItemStack()
		if(exposed.hasContainerItem) Some(new WItemStack(exposed.getContainerItem)) else None
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
	def referenceCopyOf(alternate: WItemStack): this.type =
		{is = Option(alternate.is.fold[ItemStack](null){_.copy}); this}
	def setCount(newcount: Int): this.type =
		{is.foreach(is => {if(is.isStackable) is.setCount(Math.min(countMax,newcount))}); this}
	def changeCount(countdelta: Int): this.type = setCount(this.count + countdelta)
	def withOwner[Owner <: WEntity](who: Owner) = new WItemStackOwned[Owner](asItemStack(), who)

	//TODO: Enchantment querying.
	//TODO: Item frame handling.

	def release(): Unit = {
		is = None
	}
	def destroy(): Unit = {
		is.foreach(_.setCount(0))
		release()
	}

	private[compat] def matches(b: ItemStack): Boolean = {
		asItemStack().isItemEqualIgnoreDurability(b)
	}
	def matches(b: WItemStack): Boolean = matches(b.asItemStack())

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
	def empty = new WItemStack()
	def get(inv: IInventory, index: Int): WItemStack = {
		val stack = if(index >= inv.getSizeInventory) null else inv.getStackInSlot(index)
		new WItemStack(stack)
	}
	def make(id: String): WItemStack =
		new WItemStack(CNVItem.newItemStack(id))
	private[lib] def make(logic: BLogicItem): WItemStack = {
		val item = BinderItem(logic)
		if(item == null) new WItemStack() else new WItemStack(item)
	}
	private[lib] def make(logic: BLogicBlock): WItemStack = {
		val item: Item = BinderBlock(logic)._2
		if(item == null) new WItemStack() else new WItemStack(item)
	}
}
