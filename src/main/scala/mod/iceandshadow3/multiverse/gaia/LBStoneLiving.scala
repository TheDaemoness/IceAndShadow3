package mod.iceandshadow3.multiverse.gaia

import java.util.Random

import mod.iceandshadow3.lib.LogicBlockSimple
import mod.iceandshadow3.lib.block.VarBlockBool
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, WBlockRef}
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.multiverse.DomainGaia

object LBStoneLiving {
	val varGrowing = new VarBlockBool("growing")
}
class LBStoneLiving extends LogicBlockSimple(DomainGaia, "livingstone", Materias.livingstone) {
	override protected def getVariantName(variant: Int) = ELivingstoneTypes.values()(variant).name
	override def countVariants = ELivingstoneTypes.values().length

	override def variables = Array(LBStoneLiving.varGrowing)

	override def randomlyUpdates = Some(wbs => wbs(LBStoneLiving.varGrowing).getOrElse(false))

	override def onRandomTick(variant: Int, block: WBlockRef, rng: Random) = {
		val adjb = AdjacentBlocks.Surrounding(block)
		if(adjb.forall(BlockQueries.stone)) {
			block.change(_ + (LBStoneLiving.varGrowing, false))
		} else for(neigh <- adjb) {
			if(rng.nextBoolean()) {
				val bor = neigh.promote(block)
				if(bor.isAir) bor.set(block.typeDefault)
				else bor.break(block.hardness, BlockQueries.mineableByHand(block))
			}
		}
		false
	}

	override def onReplaced(variant: Int, us: WBlockRef, them: WBlockRef, moved: Boolean): Unit = {
		for(block <- AdjacentBlocks.Surrounding(us)) {
			val lp = block.getLogicPair
			if(lp != null && lp.logic == this) {
				block.promote(us).change(_ + (LBStoneLiving.varGrowing, true))
			}
		}
	}

	override def getBlockModelGen(variant: Int) = Some(BJsonAssetGen.blockCube)
}
