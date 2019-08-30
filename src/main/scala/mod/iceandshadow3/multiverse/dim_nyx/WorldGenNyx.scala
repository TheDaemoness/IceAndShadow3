package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.base.BWorldGen
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.spatial.{Cells, PairXZ}
import mod.iceandshadow3.lib.util.collect.IMap2d
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.gaia.ELivingstoneTypes

object WorldGenNyx {
	import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
	val stones = ELivingstoneTypes.values().map(st => {new WBlockState(livingstone, st.ordinal())})
	val navistra = new WBlockState(navistra_stone, 0)
	val bedrock = new WBlockState(navistra_bedrock, 0)
	val icicles = new WBlockState(DomainNyx.Blocks.icicles, 0)
	val exousia = new WBlockState(DomainNyx.Blocks.exousia, 0)

	val yBald = 187
	val yThinning = 175
	val yFull = 167

	val yFissureFull = 147
	val yFissureMax = 171
	val yCaveMax = 147

	def stoneCommon(rng: Random) = stones(ELivingstoneTypes.getCommon(rng).ordinal())
	def stoneAny(rng: Random) = stones(ELivingstoneTypes.getAny(rng).ordinal())
}
class WorldGenNyx(seed: Long) extends BWorldGen(seed) {
	override type RegionType = NyxRegion
	override type ChunkType = NyxChunk

	val noises = new NoisesNyx(seed)

	override protected def region(coord: PairXZ) = {
		new NyxRegion(coord, noises)
	}

	override protected def chunk(xFrom: Int, zFrom: Int, regions: IMap2d[NyxRegion]) =
		new NyxChunk(xFrom, zFrom, regions(BWorldGen.toRegion(xFrom), BWorldGen.toRegion(zFrom)))

	override protected def column(xBlock: Int, zBlock: Int, chunk: NyxChunk) = {
		val cellres = chunk.isle(xBlock, zBlock)
		if(1-Cells.distance(cellres) <= 0.15) new ColumnDivide
		else {
			val cell = cellres.cellClosest
			if (cell.x == 0 && cell.z == 0) new ColumnIsleCentral(xBlock, zBlock, chunk)
			else new ColumnIsleMountainSnowyUsual(xBlock, zBlock, chunk)
		}
	}
}
