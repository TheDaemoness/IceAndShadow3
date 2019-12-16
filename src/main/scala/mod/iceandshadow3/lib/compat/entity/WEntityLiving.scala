package mod.iceandshadow3.lib.compat.entity

import mod.iceandshadow3.lib.StatusEffect
import mod.iceandshadow3.lib.compat.entity.state.{BStatus, EquipPoint, WAttribute}
import mod.iceandshadow3.lib.compat.entity.state.impl.BinderStatusEffect
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.world.WDimension
import mod.iceandshadow3.lib.spatial.{IVec3, Vec3Mutable}
import mod.iceandshadow3.lib.util.collect.{IteratorConcat, IteratorEmpty}
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

class WEntityLiving protected[entity](protected[compat] val living: LivingEntity) extends WEntity(living) {
	def sneaking = living.isSneaking
	def sprinting = living.isSprinting
	def hp: Float = living.getHealth
	def hpTemp: Float = living.getAbsorptionAmount
	def hpReal: Float = hp - hpTemp
	def hpMax: Float = living.getMaxHealth
	def undead: Boolean = living.isEntityUndead
	def heal(amount: Float = hpMax): Unit = living.heal(amount)
	def setHp(amount: Float = hpMax): Unit = living.setHealth(amount)

	def home(where: WDimension): Option[IVec3] = Option(where.getWorldSpawn)

	def facing: IVec3 = {
		val where = living.getForward
		new Vec3Mutable(
			IVec3.fromDouble(where.x),
			IVec3.fromDouble(where.y).toInt,
			IVec3.fromDouble(where.z)
		)
	}
	def facingH: IVec3 = {
		val where = living.getForward
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

	def visibleTo(who: WEntity): Boolean = living.canEntityBeSeen(who.entity)
	def equipment(where: EquipPoint): WItemStack = where.getItem(living)

	def usesAds: Boolean = false //TODO: IaS3 Armor NYI

	def findItem(itemid: String, restrictToHands: Boolean): WItemStack =
		findItem(WItemStack.make(itemid).changeOwner(this), restrictToHands)
	def findItem(tofind: WItemStack, restrictToHands: Boolean): WItemStack = {
		val tosearch = if(restrictToHands) itemsHeld() else items()
		tosearch.find{tofind.matches}.getOrElse(new WItemStack(null, living))
	}

	def itemsWorn(): Iterator[WItemStack] =
		new IteratorConcat((is: ItemStack) => {new WItemStack(is, living)}, living.getArmorInventoryList.iterator)
	def itemsHeld(): Iterator[WItemStack] =
		new IteratorConcat((is: ItemStack) => {new WItemStack(is, living)}, living.getHeldEquipment.iterator)
	def itemsEquipped(): Iterator[WItemStack] =
		new IteratorConcat((is: ItemStack) => {new WItemStack(is, living)}, living.getEquipmentAndArmor.iterator)
	def itemsStashed(): Iterator[WItemStack] =
		new IteratorConcat((is: ItemStack) => {new WItemStack(is, living)}, new IteratorEmpty[ItemStack])
	override def items(): Iterator[WItemStack] = itemsEquipped()

	def baseValue(attribute: WAttribute[this.type]): Double = living.getAttribute(attribute.attribute).getBaseValue
	def apply(attribute: WAttribute[this.type]): Double = living.getAttribute(attribute.attribute).getValue

	def apply(statusType: StatusEffect): BStatus = {
		val fx = living.getActivePotionEffect(BinderStatusEffect(statusType))
		if(fx == null) statusType.inactive else new BStatus {
			override def getEffect = statusType
			override def getTicks = fx.getDuration
			override def getAmp = fx.getAmplifier+1
			override def isAmbient = fx.isAmbient
		}
	}
	def remove(status: StatusEffect): Unit =
		if(isServerSide) living.removePotionEffect(BinderStatusEffect(status))
}
