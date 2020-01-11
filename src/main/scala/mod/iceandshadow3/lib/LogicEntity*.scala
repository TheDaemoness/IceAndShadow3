package mod.iceandshadow3.lib

import mod.iceandshadow3.lib.base.LogicCommon

sealed abstract class LogicEntity(dom: Domain, name: String) extends LogicCommon(dom, name) {
	def canBurn: Boolean
}

abstract class LogicEntitySpecial(dom: Domain, name: String) extends LogicEntity(dom, name) {
	def hasCustomModel: Boolean
}

abstract class LogicEntityProjectile(dom: Domain, name: String) extends LogicEntity(dom, name) {
	def gravity: Float
	def maxTTL = 200
	override def canBurn: Boolean = true
}

abstract class LogicEntityMob(dom: Domain, name: String)
	extends LogicEntity(dom, name)
{
	def isBoss = false
	def getMeleeAttacksToKill: Float
	def getBaseHP(zone: Int): Float = dom.tierToMobHealthFactor(this.tier, zone)
	def getXP = dom.tierToMobXP(this.tier, isBoss)
	override def canBurn: Boolean = true
}
