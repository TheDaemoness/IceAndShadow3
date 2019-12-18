package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.base.{BLogic, TLootable}

sealed abstract class BLogicEntity(dom: BDomain, name: String) extends BLogic(dom, name) {
	def canBurn: Boolean
}

abstract class BLogicEntitySpecial(dom: BDomain, name: String) extends BLogicEntity(dom, name) {
	def hasCustomModel: Boolean
}

abstract class BLogicEntityProjectile(dom: BDomain, name: String) extends BLogicEntity(dom, name) {
	def gravity: Float
	def maxTTL = 200
	override def canBurn: Boolean = true
}

abstract class BLogicEntityMob(dom: BDomain, name: String)
	extends BLogicEntity(dom, name)
	with TLootable
{
	def isBoss = false
	def getMeleeAttacksToKill: Float
	def getBaseHP(zone: Int): Float = dom.tierToMobHealthFactor(this.tier, zone)
	def getXP = dom.tierToMobXP(this.tier, isBoss)
	override def canBurn: Boolean = true
}
