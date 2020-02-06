package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.BStatusEffect
import mod.iceandshadow3.lib.compat.entity.state.{Status, BEquipPoint, WAttribute}
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.lib.compat.item.{WItemStack, WItemStackOwned}
import mod.iceandshadow3.lib.compat.world.WDimension
import mod.iceandshadow3.lib.spatial.{IVec3, Vec3Mutable}
import mod.iceandshadow3.lib.util.collect.{IteratorConcat, IteratorEmpty}
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

class WEntityLiving protected[entity](override protected[compat] val expose: LivingEntity)
extends WEntity(expose) {
	def sneaking = expose.isSneaking
	def sprinting = expose.isSprinting
	def hp: Float = expose.getHealth
	def hpTemp: Float = expose.getAbsorptionAmount
	def hpReal: Float = hp - hpTemp
	def hpMax: Float = expose.getMaxHealth
	def undead: Boolean = expose.isEntityUndead
	def heal(amount: Float = hpMax): Unit = expose.heal(amount)
	def setHp(amount: Float = hpMax): Unit = expose.setHealth(amount)

	def home(where: WDimension): Option[IVec3] = Option(where.getWorldSpawn)

	def facing: IVec3 = {
		val where = expose.getForward
		new Vec3Mutable(
			IVec3.fromDouble(where.x),
			IVec3.fromDouble(where.y).toInt,
			IVec3.fromDouble(where.z)
		)
	}
	def facingH: IVec3 = {
		val where = expose.getForward
		new Vec3Mutable(
			IVec3.fromDouble(where.x),
			0,
			IVec3.fromDouble(where.z)
		)
	}

	/** Give an item to a special inventory possessed by an entity.
		* For players, this is usually the ender chest.
		* For mobs, this may be some kind of loot-collection chest.
		*/
	def saveItem(what: WItemStack): Boolean = false

	def visibleTo(who: WEntity): Boolean = expose.canEntityBeSeen(who.expose)
	def equipment(where: BEquipPoint): WItemStackOwned[WEntityLiving] = where.getItem(expose)

	def findItem(itemid: String, restrictToHands: Boolean): WItemStackOwned[this.type] =
		findItem(WItemStack.make(itemid), restrictToHands)
	def findItem(tofind: WItemStack, restrictToHands: Boolean): WItemStackOwned[this.type] = {
		val tosearch = if(restrictToHands) itemsHeld() else items()
		tosearch.find{tofind.matches}.getOrElse(new WItemStackOwned(null, this))
	}

	def itemsWorn(): Iterator[WItemStackOwned[this.type]] =
		new IteratorConcat((is: ItemStack) => {new WItemStackOwned(is, this)}, expose.getArmorInventoryList.iterator)
	def itemsHeld(): Iterator[WItemStackOwned[this.type]] =
		new IteratorConcat((is: ItemStack) => {new WItemStackOwned(is, this)}, expose.getHeldEquipment.iterator)
	def itemsEquipped(): Iterator[WItemStackOwned[this.type]] =
		new IteratorConcat((is: ItemStack) => {new WItemStackOwned(is, this)}, expose.getEquipmentAndArmor.iterator)
	def itemsStashed(): Iterator[WItemStackOwned[this.type]] =
		new IteratorConcat((is: ItemStack) => {new WItemStackOwned(is, this)}, new IteratorEmpty[ItemStack])
	override def items(): Iterator[WItemStackOwned[this.type]] = itemsEquipped()

	def baseValue(attribute: WAttribute[this.type]): Double = expose.getAttribute(attribute.attribute).getBaseValue
	def apply(attribute: WAttribute[this.type]): Double = expose.getAttribute(attribute.attribute).getValue

	def apply(statusType: BStatusEffect): Status = {
		val fx = expose.getActivePotionEffect(BinderStatusEffect(statusType))
		if(fx == null) statusType.inactive else new Status {
			override def getEffect = statusType
			override def getTicks = fx.getDuration
			override def getAmp = fx.getAmplifier+1
			override def isAmbient = fx.isAmbient
		}
	}
	def remove(status: BStatusEffect): Unit =
		if(isServerSide) expose.removePotionEffect(BinderStatusEffect(status))


	override protected[compat] def damageItem(is: ItemStack, amount: Int): Unit = {
		is.damageItem(amount, expose, (_: LivingEntity) => ())
	}
}
