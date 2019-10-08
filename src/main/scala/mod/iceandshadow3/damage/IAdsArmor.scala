package mod.iceandshadow3.damage

/** Inherited by items, or by entities with innate armor.
	*/
trait IAdsArmor {
	def getAdsArmors: Iterable[(Class[_ <: TDmgType], AdsArmorValue)]
}
