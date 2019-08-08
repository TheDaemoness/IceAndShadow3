package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.base.{BLogic, TLootable}

sealed abstract class BLogicCommonEntity(dom: BDomain, name: String) extends BLogic(dom, name) {
	def canBurn: Boolean
}

abstract class BLogicEntity(dom: BDomain, name: String) extends BLogicCommonEntity(dom, name) {
	def hasCustomModel: Boolean
}

abstract class BLogicProjectile(dom: BDomain, name: String) extends BLogicCommonEntity(dom, name) {
	def gravity: Float
	def maxTTL = 200
	override def canBurn: Boolean = true
}

abstract class BLogicMob(dom: BDomain, name: String)
	extends BLogicCommonEntity(dom, name)
	with TLootable
{
	def isBoss(variant: Int) = false
	def getMeleeAttacksToKill(variant: Int): Float
	def getBaseHP(variant: Int, zone: Int): Float = dom.tierToMobHealthFactor(this.getTier(variant), zone)
	def getXP(variant: Int) = dom.tierToMobXP(this.getTier(variant), isBoss(variant))
	override def canBurn: Boolean = true
}
