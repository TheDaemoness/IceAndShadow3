package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.base.BWorldGen
import mod.iceandshadow3.lib.compat.block.`type`.BlockTypeSimple
import mod.iceandshadow3.lib.util.collect.IMap2d
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.gaia.ELivingstoneTypes

object WorldGenNyx {
	import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
	val stones = ELivingstoneTypes.values().map(st => {new BlockTypeSimple(livingstone, st.ordinal())})
	val navistra = new BlockTypeSimple(navistra_stone, 0)
	val bedrock = new BlockTypeSimple(navistra_bedrock, 0)
	val icicles = new BlockTypeSimple(DomainNyx.Blocks.icicles, 0)
	val exousia = new BlockTypeSimple(DomainNyx.Blocks.exousia, 0)

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
	override type RegionDataType = RegionInterpret
	override type RegionInterpretType = RegionInterpret

	val noises = new NoisesNyx(seed)

	override protected def region(xFrom: Int, zFrom: Int) = {
		new RegionInterpret(xFrom, zFrom, noises)
	}

	override protected def interpret(xRegion: Int, zRegion: Int, regions: IMap2d[RegionInterpret]) =
		regions(xRegion, zRegion)

	override protected def column(xBlock: Int, zBlock: Int, region: RegionInterpret) = {
		val cell = region.islemap(xBlock, zBlock).cellClosest
		if(cell.x == 0 && cell.z == 0) new ColumnCentral(xBlock, zBlock, region)
		else new ColumnMountainSnowyUsual(xBlock, zBlock, region)
	}
}
