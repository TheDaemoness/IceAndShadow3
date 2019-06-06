package mod.iceandshadow3.damage

import mod.iceandshadow3.compat.entity.WEntityLiving
import mod.iceandshadow3.compat.item.WRefItem


sealed trait TDmgType {
	def onDamage(dmg: Float, dmgResisted: Float, what: WRefItem): Float
	def onDamage(dmg: Float, dmgResisted: Float, who: WEntityLiving): Float = dmgResisted
	def resistancePotMult(amp: Int): Float = Math.max(0, 1-amp/5f)
}

abstract class BDamage extends TDmgType {
	def baseDamage: Float
}

trait TDmgTypeOmni extends TDmgType {this: BDamage => def name = "omni"}

trait TDmgTypeKinetic extends TDmgTypeOmni {this: BDamage => override def name = "kinetic"}
trait TDmgTypeImpact extends TDmgTypeKinetic {this: BDamage => override def name = "impact"}
trait TDmgTypePhysical extends TDmgTypeKinetic {this: BDamage => override def name = "physical"}
trait TDmgTypeSharp extends TDmgTypePhysical {this: BDamage => override def name = "sharp"}
trait TDmgTypeBlunt extends TDmgTypePhysical {this: BDamage => override def name = "blunt"}
trait TDmgTypeBlast extends TDmgTypePhysical {this: BDamage => override def name = "blast"}

trait TDmgTypeMagic extends TDmgTypeOmni {this: BDamage => override def name = "magic"}
trait TDmgTypeCold extends TDmgTypeOmni {this: BDamage => override def name = "cold"}
trait TDmgTypeHeat extends TDmgTypeOmni {this: BDamage => override def name = "heat"}
trait TDmgTypeExousic extends TDmgTypeOmni {this: BDamage => override def name = "exousic"
	override def onDamage(dmg: Float, dmgResisted: Float, what: WRefItem):Float = {
		what.consume(dmgResisted.toInt)
		dmgResisted
	}
}

trait TDmgTypeIce extends TDmgTypeSharp with TDmgTypeCold {this: BDamage => override def name = "ice"
	override def onDamage(dmg: Float, dmgResisted: Float, who: WEntityLiving):Float = {
		//TODO: Status effect.
		dmgResisted/2
	}
}

trait TDmgTypeEldritch extends TDmgTypeOmni {this: BDamage => override def name = "eldritch"
	override def resistancePotMult(amp: Int): Float = 1f
}
trait TDmgTypeShadow extends TDmgTypeEldritch {this: BDamage => override def name = "shadow"}