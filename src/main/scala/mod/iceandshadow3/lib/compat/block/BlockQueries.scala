package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.block.impl.BVarBlock
import net.minecraft.block.AbstractFurnaceBlock
import net.minecraft.block.material.Material

object BlockQueries {
	def stone(bv: WBlockState) = bv.exposeBS().getMaterial == Material.ROCK
	def sand(bv: WBlockState): Boolean = bv.exposeBS().getMaterial == Material.SAND
	def glass(bv: WBlockState): Boolean = bv.exposeBS().getMaterial == Material.GLASS
	def solid(bv: WBlockState) = bv.exposeBS().isSolid
	def power(bv: WBlockState) = bv.exposeBS().canProvidePower
	def mineableByHand(bv: WBlockState) = bv.exposeBS().getMaterial.isToolNotRequired
	def mineableByWood(bv: WBlockState) = bv.exposeBS().getHarvestLevel <= 0
	def mineableByStone(bv: WBlockState) = bv.exposeBS().getHarvestLevel <= 1
	def mineableByIron(bv: WBlockState) = bv.exposeBS().getHarvestLevel <= 2
	def mineableByDiamond(bv: WBlockState) = bv.exposeBS().getHarvestLevel <= 3

	def materia(materia: Materia): WBlockState => Boolean =
		bv => Option(bv.getLogicPair).fold(false)(lp => {lp.logic.materia.isTypeOf(materia)})
	def notTougher(bs: WBlockState): WBlockState => Boolean =
		_.exposeBS().getHarvestLevel >= bs.exposeBS().getHarvestLevel
	def isFurnace: WBlockState => Boolean =
		bl => bl.exposeBS().getBlock.isInstanceOf[AbstractFurnaceBlock]

	def notHarder(hardness: Float): WBlockView => Boolean = _.hardness <= hardness
	def notSofter(hardness: Float): WBlockView => Boolean = _.hardness >= hardness
	def crushableBy(what: WBlockView): WBlockView => Boolean = v => { v.hardness < what.hardness }
	def hasLogic(bl: BLogicBlock): WBlockView => Boolean = bv => {
		val lp = bv.getLogicPair
		lp != null && lp.logic == bl
	}
	def varMatches[T](variable: BVarBlock[T], pred: T => Boolean): WBlockView => Boolean = {
		wbv => wbv ? (variable, pred)
	}
}
