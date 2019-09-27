package mod.iceandshadow3.damage

import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityLiving}
import mod.iceandshadow3.lib.compat.item.WItemStack
sealed trait TDmgType {
	def onDamageArmor(dmgThrough: Float, dmgAbsorbed: Float, what: WItemStack): Float
	def onDamageEntity(dmgThrough: Float, dmgAbsorbed: Float, who: WEntityLiving): Float
}

abstract class BDamage extends TDmgType {
	def amount(target: WEntity): Float
	def name: String
}

//Minor departure from the IaS3 naming scheme.
abstract class Damage(damage: Float) extends BDamage {
	final def amount(target: WEntity) = damage
	def name: String
}

trait TDmgTypeOmni extends TDmgType {
	this: Damage => override def name = "omni"
	override def onDamageArmor(dmgThrough: Float, dmgResisted: Float, what: WItemStack): Float = dmgResisted/3f
	override def onDamageEntity(dmgThrough: Float, dmgResisted: Float, who: WEntityLiving): Float = dmgThrough
}

//Supertype of all damage types resisted by resistance potions.
trait TDmgTypeNatural extends TDmgTypeOmni {this: Damage => override def name = "natural"}

trait TDmgTypeKinetic extends TDmgTypeNatural {this: Damage => override def name = "kinetic"}
trait TDmgTypeImpact extends TDmgTypeKinetic {this: Damage => override def name = "impact"}
trait TDmgTypeSonic extends TDmgTypeKinetic {this: Damage => override def name = "sonic"}
trait TDmgTypePhysical extends TDmgTypeKinetic {this: Damage => override def name = "physical"}
trait TDmgTypeSharp extends TDmgTypePhysical {this: Damage => override def name = "sharp"}
trait TDmgTypeBlunt extends TDmgTypePhysical {this: Damage => override def name = "blunt"}
trait TDmgTypeBlast extends TDmgTypePhysical {this: Damage => override def name = "blast"}

trait TDmgTypeEnergy extends TDmgTypeNatural {this: Damage => override def name = "energy"}
trait TDmgTypeSpark extends TDmgTypeEnergy {this: Damage => override def name = "spark"}
trait TDmgTypeThermal extends TDmgTypeEnergy {this: Damage => override def name = "thermal"}
trait TDmgTypeCold extends TDmgTypeThermal {this: Damage => override def name = "cold"}
trait TDmgTypeHeat extends TDmgTypeThermal {this: Damage => override def name = "heat"}
trait TDmgTypeMagic extends TDmgTypeNatural {this: Damage => override def name = "magic"}

trait TDmgTypeIce extends TDmgTypeSharp with TDmgTypeCold {this: Damage => override def name = "ice"
	override def onDamageEntity(dmg: Float, dmgResisted: Float, who: WEntityLiving):Float = {
		//TODO: Status effect that does half the damage.
		dmg
	}
}

trait TDmgTypeExousic extends TDmgTypeOmni {this: Damage => override def name = "exousic"
	override def onDamageArmor(dmg: Float, dmgResisted: Float, what: WItemStack):Float = dmg*5
}

trait TDmgTypeEldritch extends TDmgTypeOmni {this: Damage => override def name = "eldritch"}
trait TDmgTypeShadow extends TDmgTypeEldritch {this: Damage => override def name = "shadow"}