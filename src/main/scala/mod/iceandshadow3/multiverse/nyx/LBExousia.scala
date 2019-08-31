package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.damage.{Attack, AttackForm, BDamage, TDmgTypeExousic}
import mod.iceandshadow3.lib.BLogicBlockSimple
import mod.iceandshadow3.lib.block.{BlockShape, IMateria}
import mod.iceandshadow3.lib.compat.block.{BMateriaPlasma, WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityItem, WEntityLiving, WProjectile}
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.spatial.UnitVec3s
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.misc.{Particles, Statuses}

object LBExousia {
	val tagResist = "iceandshadow3:resists_exousia"
}
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
		import LBExousia._
		who.slow(1d, 4d, 1d)
		if(who match {
			case items: WEntityItem =>
				!items.item.hasTag(tagResist) &&
				!items.item.toBlockState.fold(false)(_.hasTag(tagResist))
			case _ => true
		}) {
			who.playSound(WSound("minecraft:entity.generic.burn"), 0.5f, who.rng(0.9f, 0.2f))
			//TODO: Damage resistance check.
			who match {
				case victim: WEntityLiving => victim.damageWithStatus(damage, 1f, Statuses.wither, 115, 2)
				case missile: WProjectile => missile.remove()
				case _ => who.damage(damage)
			}
			who.particle(Particles.smoke_large, UnitVec3s.ZERO)
			who.impulse(0, 0.05, 0)
		}
	}

	override def isTechnical = true

	lazy val blocktype = new WBlockState(this, 0)
	override def onNeighborChanged(variant: Int, us: WBlockRef, them: WBlockRef): WBlockState = {
		if(them.posFine.yBlock <= us.posFine.yBlock && !them.hasTag(LBExousia.tagResist)) {
			if(!them.isAir) {
				//TODO: Previous particle effects were placeholder AND looked bad. Make better ones.
				them.playSound(WSound("minecraft:entity.generic.burn"), 1f, them.rng(0.9f, 0.2f))
			}
			them.set(blocktype)
		}
		us
	}

	override def shouldHaveLootTable = E3vl.FALSE

	//TODO: Exousia spreading, fogging.
}
