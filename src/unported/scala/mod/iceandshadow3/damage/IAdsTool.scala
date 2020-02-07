package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.WEntity

trait IAdsTool extends IAdsArmor {
	def canBlock: Boolean
	def getAdsArmors: Iterable[(Class[_ <: TDmgType], AdsArmorValue)] = List()
	def getAttack(target: WEntity): Attack
}
