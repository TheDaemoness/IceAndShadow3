package mod.iceandshadow3.multiverse.nyx

import mod.iceandshadow3.damage.{Attack, AttackForm, DamageWithStatus, TDmgTypeExousic}
import mod.iceandshadow3.lib.{LogicBlock, LogicBlockTechnical}
import mod.iceandshadow3.lib.block.BlockShape
import mod.iceandshadow3.lib.compat.block.{Materia, WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityItem, WEntityLiving, WProjectile}
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.lib.compat.world.WSound
import mod.iceandshadow3.lib.spatial.UnitVec3s
import mod.iceandshadow3.lib.util.E3vl
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.misc.{Particles, StatusEffects}

object LBExousia {
	val materia = Materia.builder(Materia.plasma).luma(9)("exousia")
	val tagResist = "iceandshadow3:resists_exousia"
}
class LBExousia extends LogicBlockTechnical(DomainNyx, "exousia", LBExousia.materia) {
	val damage = Attack(
		"exousia", AttackForm.VOLUME,
		new DamageWithStatus(6f, StatusEffects.exousia.forTicks(119)) with TDmgTypeExousic
	)
	override def harvestOverride(block: WBlockRef, fortune: Int) = Array()

	override val shape: BlockShape = BlockShape.EMPTY

	override def onInside(block: WBlockRef, who: WEntity): Unit = {
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
				case victim: WEntityLiving => damage(victim)
				case missile: WProjectile => missile.remove()
				case _ => damage(_)
			}
			who.particle(Particles.smoke_large, UnitVec3s.ZERO)
			who.impulse(0, 0.025, 0)
		}
	}

	override def onNeighborChanged(us: WBlockRef, them: WBlockRef): WBlockState = {
		if(them.posFine.yBlock <= us.posFine.yBlock && !them.hasTag(LBExousia.tagResist)) {
			if(!them.isAir) {
				//TODO: Previous particle effects were placeholder AND looked bad. Make better ones.
				them.playSound(WSound("minecraft:entity.generic.burn"), 1f, them.rng(0.9f, 0.2f))
			}
			them.set(this.toWBlockState)
		}
		us
	}

	override def shouldHaveLootTable = E3vl.FALSE
	override def getBlockModelGen = Some(BJsonAssetGen.blockCube)

	//TODO: Exousia spreading, fogging.
}
