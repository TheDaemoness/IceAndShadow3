package mod.iceandshadow3.multiverse.gaia

import java.util.Random

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.block.VarBlockBool
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.block.{AdjacentBlocks, BlockQueries, WBlockRef}
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.lib.compat.recipe.ECraftingType
import mod.iceandshadow3.multiverse.{DomainGaia, DomainPolis}

object LBStoneLiving {
	val varGrowing = new VarBlockBool("growing")
}
class LBStoneLiving(variant: ELivingstoneTypes)
extends LogicBlock(DomainGaia, "livingstone_"+variant.name, Materias.livingstone) {

	override val variables = Array(LBStoneLiving.varGrowing)

	override def randomlyUpdates = Some(wbs => wbs(LBStoneLiving.varGrowing).getOrElse(false))

	override def onRandomTick(block: WBlockRef, rng: Random) = {
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

	override def onReplaced(us: WBlockRef, them: WBlockRef, moved: Boolean): Unit = {
		for(block <- AdjacentBlocks.Surrounding(us)) {
			val logic = block.getLogic
			if(logic != null && logic.variables.contains(LBStoneLiving.varGrowing)) {
				block.promote(us).change(_ + (LBStoneLiving.varGrowing, true))
			}
		}
	}

	override def getBlockModelGen = Some(BJsonAssetGen.blockCube)

	Registrar.addRecipeCallback("smelting."+name, name => {
		ECraftingType.COOK_SMELT.apply(name, ECraftingType.About(
			DomainPolis.Blocks.polished_stones(variant.ordinal()).toWItemStack
		), this.toWItemStack)
	})

	Registrar.addRecipeCallback(s"craft.$name.grow", name => {
		val minerals = DomainGaia.Items.minerals
		ECraftingType.CRAFT_SHAPELESS(name,
			ECraftingType.About(this.toWItemStack.setCount(8)),
			DomainGaia.Items.shales(variant.ordinal).toWItemType,
			minerals, minerals, minerals, minerals,
			minerals, minerals, minerals, minerals
		)
	})
}
