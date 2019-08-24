package mod.iceandshadow3.lib.compat.block

import mod.iceandshadow3.lib.block.IMateria
import net.minecraft.block.material.Material

object BlockQueries {
	def stone(bv: WBlockView) = bv.exposeBS().getMaterial == Material.ROCK
	def sand(bv: WBlockView): Boolean = bv.exposeBS().getMaterial == Material.SAND
	def solid(bv: WBlockView) = bv.exposeBS().isSolid
	def power(bv: WBlockView) = bv.exposeBS().canProvidePower
	def mineableByWood(bv: WBlockView) = bv.exposeBS().getHarvestLevel <= 0
	def mineableByStone(bv: WBlockView) = bv.exposeBS().getHarvestLevel <= 1
	def mineableByIron(bv: WBlockView) = bv.exposeBS().getHarvestLevel <= 2
	def mineableByDiamond(bv: WBlockView) = bv.exposeBS().getHarvestLevel <= 3
	def notHarder(hardness: Float): WBlockView => Boolean = _.getHardness <= hardness
	def notSofter(hardness: Float): WBlockView => Boolean = _.getHardness >= hardness
	def crushableBy(what: WBlockView): WBlockView => Boolean = v => { v.getHardness < what.getHardness }
	def materia(mat: Class[_ <: IMateria]): WBlockView => Boolean = {
		bv => Option(bv.getLogicPair).fold(false)({_.logic.isOfMateria(mat)})
	}
}
