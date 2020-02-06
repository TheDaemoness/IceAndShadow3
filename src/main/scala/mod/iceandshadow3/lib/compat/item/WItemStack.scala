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

/** Null-safe item stack.
	*/
class WItemStack(inputstack: ItemStack)
	extends WItem
	with TWLogical[BLogicItem]
	with TLocalized
	with TNbtSource
{
	protected var is = if(inputstack == null) ItemStack.EMPTY else inputstack
	def this() = this(ItemStack.EMPTY)
	def this(is: IItemProvider) = this(new ItemStack(is))

	def copy: WItemStack = new WItemStack(is.copy())
	override def isEmpty: Boolean = is.isEmpty
	def count: Int = is.getCount
	def countMax: Int = if(is == ItemStack.EMPTY) 0 else is.getMaxStackSize
	def hasOwner: Boolean = false
	def getDamage: Int = is.getDamage
	def getDamageMax: Int = if(is == ItemStack.EMPTY) 0 else is.getMaxDamage
	def setDamage(amount: Int): this.type = {
		if(is != ItemStack.EMPTY) expose().setDamage(amount)
		this
	}
	/** If this item stack has a "container" that should be left in crafting (e.g. a bottle), return it. */
	def getContainerStack: Option[WItemStack] = {
		val exposed = expose()
		if(exposed.hasContainerItem) Some(new WItemStack(exposed.getContainerItem)) else None
	}

	def isComplex: Boolean = is.hasTag

	def expose(): ItemStack = is

	/** Changes the item stack referenced by this WItemStack to a copy of a different item stack.
		* IMPORTANT: This does NOT change the item internally!
		*/
	def referenceCopyOf(alternate: WItemStack): this.type =
		{is = alternate.expose().copy(); this}
	def setCount(newcount: Int): this.type = {
		if(is != ItemStack.EMPTY && is.isStackable) is.setCount(Math.min(countMax,newcount)); this
	}
	def changeCount(countdelta: Int): this.type = setCount(this.count + countdelta)
	def withOwner[Owner <: WEntity](who: Owner) = new WItemStackOwned[Owner](expose(), who)

	//TODO: Enchantment querying.
	//TODO: Item frame handling.

	/** Release this WItemStack's reference to the specified stack. */
	def release(): Unit = {
		is = ItemStack.EMPTY
	}
	/** Makes the referenced itemstack (w/ stack size of 0) and releases. */
	def destroy(): Unit = {
		if(!is.isEmpty) is.setCount(0)
		release()
	}
	/** As destroy, but returns a copy of the stack before destroy was called. */
	protected[compat] def copyAndDestroy(): ItemStack = {
		val retval = expose().copy()
		destroy()
		retval
	}

	private[compat] def matches(b: ItemStack): Boolean = {
		expose().isItemEqualIgnoreDurability(b)
	}
	def matches(b: WItemStack): Boolean = matches(b.expose())

	override protected def exposeCompoundOrNull() = if(is == ItemStack.EMPTY) null else is.getOrCreateTag();

	def getBurnTicks: Int = is.getBurnTime

	override protected[compat] def getLocalizedName = expose().getTextComponent

	override def asItem() = expose().getItem
	override def asWItem(): WItemType = WItemType(asItem())

	override protected[compat] def exposeNbt() =
		if(is == ItemStack.EMPTY) new CompoundNBT else is.getOrCreateTag();
	override protected[compat] def setNbt(what: CompoundNBT): Unit =
		if(is != ItemStack.EMPTY) expose().setTag(what)

	def apply(attr: WAttribute[_], where: EquipPointVanilla): AttributeModTotal =
		AttributeModTotal(expose().getAttributeModifiers(where.where).get(attr.name))
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
