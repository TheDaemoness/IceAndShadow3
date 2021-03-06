package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.state.{BEquipPoint, EquipPointVanilla, WAttribute}
import mod.iceandshadow3.lib.compat.item.WItemStack

case class AdsArmorValue(hard: Float, soft: Float) {
	private final val multiplier = {
		val tmpdivisor = (hard+soft)/2f //TODO: Nerf?
		if(tmpdivisor < 0) 1-tmpdivisor/2 else 1/(1+tmpdivisor)
	}
	def apply(dmg: Float): Float = Math.max(dmg-hard, 0)*multiplier
	def soaked(dmg: Float): Float = dmg - dmg*multiplier //NOTE: Excludes portion absorbed by hard armor.
	def +(b: AdsArmorValue) = AdsArmorValue(hard+b.hard, soft+b.soft)
}
object AdsArmorValue {
	type Values = Iterable[(Class[_ <: TDmgType], AdsArmorValue)]
	val NONE = AdsArmorValue(0f, 0f)
	val ABSOLUTE = AdsArmorValue(Float.MaxValue, 0f)
	val ARMOR_DEFAULT: Values = List()
	val SHIELD_DEFAULT: Values = List(
		(classOf[TDmgTypePhysical], AdsArmorValue(2, 0)),
		(classOf[TDmgTypeSharp], AdsArmorValue(2, 0))
	)
	def getFrom(what: WItemStack, where: BEquipPoint): Values = {
		val innate = where match {
			case vanilla: EquipPointVanilla => fromVanilla(what, vanilla)
			case _ => List()
		}
		innate ++ what.facet[IAdsArmor].fold(ARMOR_DEFAULT)(_.getAdsArmors)
	}
	def getFromShield(what: WItemStack): Values = {
		if(!what.isEmpty) List()
		else SHIELD_DEFAULT ++ what.facet[IAdsArmor].fold(ARMOR_DEFAULT)(_.getAdsArmors)
	}
	def apply(what: Damage, armors: Values): AdsArmorValue = {
		var soft = 0f
		var hard = 0f
		for(armor <- armors) {
			if(armor._1.isInstance(what)) {
				soft += armor._2.soft
				hard += armor._2.hard
			}
		}
		AdsArmorValue(hard, soft)
	}
	private def fromVanilla(what: WItemStack, where: EquipPointVanilla): Values = {
		val factor = if(where == BEquipPoint.BODY_CHEST || where == BEquipPoint.BODY_LEGS) 1 else 2
		val armor = what.apply(WAttribute.ARMOR_VANILLA, where).apply(0d)*factor
		val toughness = what.apply(WAttribute.ARMOR_VANILLA_TOUGHNESS, where).apply(0f)
		val hard = Math.floor(armor/4).toFloat
		val soft = (armor - hard + toughness).toFloat
		val shade = if(hard > 0) -2f else if(soft > 0) -1f else 0f
		val physical = (classOf[TDmgTypePhysical], AdsArmorValue(hard, soft))
		val antishadow = (classOf[TDmgTypeShadow], AdsArmorValue(shade, 0))
		//TODO: Allow logics to add armor values at this point.
		List(physical, antishadow)
	}
}
