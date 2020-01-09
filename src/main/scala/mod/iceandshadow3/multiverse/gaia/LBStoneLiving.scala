package mod.iceandshadow3.multiverse.gaia

import java.util.Random

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.block.VarBlockBool
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, WBlockRef, WBlockState}
import mod.iceandshadow3.lib.compat.loot.{BLoot, LootBuilder, WLootContextBlock}
import mod.iceandshadow3.lib.data.VarSet
import mod.iceandshadow3.multiverse.DomainGaia

object LBStoneLiving {
	val varGrowing = new VarBlockBool("growing")
}
class LBStoneLiving(variant: ELivingstoneType)
extends LogicBlock(DomainGaia, "livingstone_"+variant.name, Materias.livingstone) {
	override def tier = variant.rarity-1
	override val variables = VarSet(LBStoneLiving.varGrowing)

	override def randomlyUpdates = Some(wbs => wbs(LBStoneLiving.varGrowing).getOrElse(false))

	override def onRandomTick(us: WBlockRef, rng: Random) = {
		val adjb = AdjacentBlocks.Surrounding(us)
		if(adjb.forall(BlockQueries.stone)) {
			us.change(_ + (LBStoneLiving.varGrowing, false))
		} else for(neigh <- adjb) {
			if(rng.nextBoolean()) {
				val bor = neigh.promote(us)
				if(bor.isAir) bor.set(us.typeDefault)
				else bor.break(us.hardness, BlockQueries.mineableByHand(us))
			}
		}
		false
	}

	override def onReplaced(us: WBlockState, them: WBlockRef, moved: Boolean): Unit = {
		for(block <- AdjacentBlocks.Surrounding(them)) {
			val logic = block.getLogic
			if(logic != null && logic.variables.contains(LBStoneLiving.varGrowing)) {
				block.promote(them).change(_ + (LBStoneLiving.varGrowing, true))
			}
		}
	}

	override def addDrops(what: LootBuilder[WLootContextBlock]): Unit = what.addOne(
		BLoot.silktouch(this).orElse(
			BLoot(DomainGaia.Items.minerals).chance(0.5f)
		).orElse(
			BLoot(DomainGaia.Items.shales(variant.ordinal())).chance(0.125f) //TODO: Fortune.
		)
	)
}
