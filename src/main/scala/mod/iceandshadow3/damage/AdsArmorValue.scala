package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.WAttribute
import mod.iceandshadow3.lib.compat.entity.state.{EquipPoint, EquipPointVanilla}
import mod.iceandshadow3.lib.compat.item.WItemStack

case class AdsArmorValue(hard: Float, soft: Float) {
	private val divisor = (hard+soft)/2f
	def apply(dmg: Float): Float = {
		val basereduced = Math.max(dmg-hard, 0)
		if(divisor >= 0) basereduced/(1+divisor) else basereduced*(1-divisor/2)
	}
	def +(b: AdsArmorValue) = AdsArmorValue(hard+b.hard, soft+b.soft)
}
object AdsArmorValue {
	type Values = Iterable[(Class[_ <: TDmgTypeOmni], AdsArmorValue)]
	val NONE = AdsArmorValue(0f, 0f)
	val ABSOLUTE = AdsArmorValue(Float.MaxValue, 0f)
	val SHIELD_DEFAULT = List(
		(classOf[TDmgTypePhysical], AdsArmorValue(2, 0)),
		(classOf[TDmgTypeSharp], AdsArmorValue(2, 0))
	)
	def getFrom(what: WItemStack, where: EquipPoint): Values = {
		val innate = where match {
			case vanilla: EquipPointVanilla => fromVanilla(what, vanilla)
			case _ => List()
		}
		innate
	}
	def getFromShield(what: WItemStack): Values = {
		if(!what.isEmpty) List()
		else SHIELD_DEFAULT
	}
	def apply(what: BDamage, armors: Values): AdsArmorValue = {
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
		val factor = if(where == EquipPoint.BODY_CHEST || where == EquipPoint.BODY_LEGS) 1 else 2
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
