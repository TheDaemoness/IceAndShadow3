package mod.iceandshadow3.multiverse.nyx

import java.util.Random

import mod.iceandshadow3.damage.{Attack, AttackForm, DamageWithStatus, TDmgTypeExousic}
import mod.iceandshadow3.lib.LogicBlockTechnical
import mod.iceandshadow3.lib.block.BlockShape
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, Materia, WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.entity.{WEntity, WEntityItem, WProjectile}
import mod.iceandshadow3.lib.compat.id.{WIdTagBlock, WIdTagItem}
import mod.iceandshadow3.lib.compat.world.{TWWorldPlace, WSound}
import mod.iceandshadow3.lib.spatial.{IPositionalFine, UnitVec3s}
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.misc.{Particles, StatusEffects}

object LBExousia {
	val materia = Materia.builder(Materia.plasma).luma(9)("exousia")
	val tagItemResist = WIdTagItem("iceandshadow3:resists_exousia")
	val tagBlockResist = WIdTagBlock("iceandshadow3:resists_exousia")
	val spreadDelayTicks = 4
}
class LBExousia extends LogicBlockTechnical(DomainNyx, "exousia", LBExousia.materia) {
	val damage = Attack(
		"exousia", AttackForm.VOLUME,
		new DamageWithStatus(6f, StatusEffects.exousia.forTicks(119)) with TDmgTypeExousic
	)

	override val shape: BlockShape = BlockShape.EMPTY

	final override val handlerEntityInside = (us: WBlockRef, who: WEntity) => {
		import LBExousia._
		who.slow(0.5d, 0.5d, 0.5d)
		who.impulse(0, 0.01, 0)
		if(who match {
			case items: WEntityItem =>
				!tagItemResist.unapply(items.item) &&
				!items.item.toBlockState.fold(false)(resistsExousia)
			case _ => true
		}) {
			//TODO: Damage resistance check.
			burnFx(who)
			who.impulse(0, 0.01, 0)
			who match {
				case missile: WProjectile => missile.remove()
				case _ => damage(who)
			}
		}
	}

	override def onAdded(us: WBlockRef, them: WBlockState, moving: Boolean): Unit = {
		us.scheduleTick(LBExousia.spreadDelayTicks)
	}

	override def onReplaced(us: WBlockState, them: WBlockRef, moved: Boolean): Unit = {
		if(!resistsExousia(them)) {
			if(!them.isAir) burnFx(them)
			them.set(CommonBlockTypes.AIR)
		}
	}

	override def onNeighborChanged(us: WBlockRef, them: WBlockRef): WBlockState = {
		us.scheduleTick(LBExousia.spreadDelayTicks)
		us
	}

	override def onUpdateTick(us: WBlockRef, rng: Random): Unit = {
		for(them <- AdjacentBlocks.Cupping.apply(us)) {
			if(!resistsExousia(them)) {
				val themRef = them.promote(us)
				if(!them.isAir) burnFx(themRef)
				themRef.set(this.toWBlockState)
			}
		}
	}

	def resistsExousia(them: WBlockState): Boolean =
		them.getLogic == this || LBExousia.tagBlockResist.unapply(them)

	def burnFx(where: TWWorldPlace with IPositionalFine): Unit = {
		//TODO: Better particles
		where.particle(Particles.smoke_large, where.posFine, UnitVec3s.ZERO)
		where.playSound(WSound("minecraft:entity.generic.burn"), 1f, where.rng(0.9f, 0.2f))
	}

	//TODO: fogging.
}
