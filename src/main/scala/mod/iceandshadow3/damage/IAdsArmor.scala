package mod.iceandshadow3.damage

import scala.reflect.ClassTag

/** Inherited by items, or by entities with innate armor.
	*/
trait IAdsArmor {
	def getAdsArmor[T <: TDmgTypeOmni: ClassTag]: AdsArmorValue
}
