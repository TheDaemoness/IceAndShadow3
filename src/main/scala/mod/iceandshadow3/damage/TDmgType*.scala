package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.WEntityLiving
import mod.iceandshadow3.lib.compat.item.WItemStack
sealed trait TDmgType {
	def onDamageArmor(dmgThrough: Float, dmgAbsorbed: Float, what: WItemStack): Float
	def onDamageEntity(dmgThrough: Float, dmgAbsorbed: Float, who: WEntityLiving): Float
}

trait TDmgTypeOmni extends TDmgType {
	this: BDamage => override def name = "omni"
	override def onDamageArmor(dmgThrough: Float, dmgResisted: Float, what: WItemStack): Float = dmgResisted
	override def onDamageEntity(dmgThrough: Float, dmgResisted: Float, who: WEntityLiving): Float = dmgThrough
}

//Supertype of all damage types resisted by resistance potions.
trait TDmgTypeNatural extends TDmgTypeOmni {this: BDamage => override def name = "natural"}

trait TDmgTypeKinetic extends TDmgTypeNatural {this: BDamage => override def name = "kinetic"}
trait TDmgTypeImpact extends TDmgTypeKinetic {this: BDamage => override def name = "impact"}
trait TDmgTypeSonic extends TDmgTypeKinetic {this: BDamage => override def name = "sonic"}
trait TDmgTypePhysical extends TDmgTypeKinetic {this: BDamage => override def name = "physical"}
trait TDmgTypeSharp extends TDmgTypePhysical {this: BDamage => override def name = "sharp"}
trait TDmgTypeBlunt extends TDmgTypePhysical {this: BDamage => override def name = "blunt"}
trait TDmgTypeBlast extends TDmgTypePhysical {this: BDamage => override def name = "blast"}

trait TDmgTypeEnergy extends TDmgTypeNatural {this: BDamage => override def name = "energy"}
trait TDmgTypeSpark extends TDmgTypeEnergy {this: BDamage => override def name = "spark"}
trait TDmgTypeThermal extends TDmgTypeEnergy {this: BDamage => override def name = "thermal"}
trait TDmgTypeCold extends TDmgTypeThermal {this: BDamage => override def name = "cold"}
trait TDmgTypeHeat extends TDmgTypeThermal {this: BDamage => override def name = "heat"}
trait TDmgTypeMagic extends TDmgTypeNatural {this: BDamage => override def name = "magic"}

trait TDmgTypeIce extends TDmgTypeSharp with TDmgTypeCold {this: BDamage => override def name = "ice"}

trait TDmgTypeExousic extends TDmgTypeOmni {this: BDamage => override def name = "exousic"
	override def onDamageArmor(dmg: Float, dmgResisted: Float, what: WItemStack):Float = dmg*15
}

trait TDmgTypeEldritch extends TDmgTypeOmni {this: BDamage => override def name = "eldritch"}
trait TDmgTypeShadow extends TDmgTypeEldritch {this: BDamage => override def name = "shadow"}