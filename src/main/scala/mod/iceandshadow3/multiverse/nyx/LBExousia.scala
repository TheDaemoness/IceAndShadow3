package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.basics.BLogicBlockSimple
import mod.iceandshadow3.basics.block.{BlockShape, IMateria}
import mod.iceandshadow3.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.compat.block.WBlockRef
import mod.iceandshadow3.compat.block.impl.BMateriaPlasma
import mod.iceandshadow3.compat.entity.{WEntity, WEntityLiving, WProjectile}
import mod.iceandshadow3.compat.world.WSound
import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeExousic}
import mod.iceandshadow3.spatial.UnitVec3s
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.misc.{Particles, Statuses}

class LBExousia extends BLogicBlockSimple(DomainNyx, "exousia", new BMateriaPlasma {
	override def getName = "exousia"
	override def getBaseLuma = 9
	override def getBaseHardness = 0
	override def getBaseBlastResist = IMateria.indestructibleByBlast
	override def getBaseHarvestResist = -1
	override def getShapes = Set()
}) {
	val damage = new Attack("exousia", AttackForm.VOLUME, new BDamage with TDmgTypeExousic {
		override def baseDamage = 6f
	})
	override def harvestOverride(variant: Int, block: WBlockRef, fortune: Int) = Array()

	override val shape: BlockShape = BlockShape.EMPTY

	override def onInside(variant: Int, block: WBlockRef, who: WEntity): Unit = {
		who.playSound(WSound.lookup("minecraft:entity.generic.burn"), 0.5f, who.rng(0.9f, 0.2f))
		//TODO: Damage resistance check.
		who.particle(Particles.smoke_large, UnitVec3s.ZERO)
		who.damage(damage)
		who.slow(0.5, 0.1, 0.5)
		who.impulse(0, 0.1, 0)
		who match {
			case victim: WEntityLiving => victim.setStatus(Statuses.wither, 115, 2)
			case missile: WProjectile => missile.remove()
			case _ =>
		}
	}

	override def isTechnical = true

	lazy val blocktype = new BlockTypeSimple(this, 0)
	override def onNeighborChanged(variant: Int, us: WBlockRef, them: WBlockRef): Unit = {
		if(them.position.yBlock <= us.position.yBlock && !them.resistsExousia) {
			if(!them.isAir) {
				them.particle(Particles.smoke_large, UnitVec3s.ZERO)
				them.playSound(WSound.lookup("minecraft:entity.generic.burn"), 1f, them.rng(0.9f, 0.2f))
			}
			them.set(blocktype)
		}
	}

	//TODO: Exousia spreading, fogging.
}
