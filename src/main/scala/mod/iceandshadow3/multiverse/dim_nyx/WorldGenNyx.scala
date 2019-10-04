package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.gen.{BWorldGen, WorldGenLayerTerrain}
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

	val yExousia = 8
	val yNavistraExtra = 2
	val yFissureFull = 147
	val yFissureMax = 171
	val yCaveMax = 147

	def stoneCommon(rng: Random) = stones(ELivingstoneTypes.getCommon(rng).ordinal())
	def stoneAny(rng: Random) = stones(ELivingstoneTypes.getAny(rng).ordinal())

	def defaultBlock(y: Int): WBlockState =
		if(y == 0) WorldGenNyx.bedrock else if(y <= yExousia) WorldGenNyx.exousia else CommonBlockTypes.AIR
}
final class WorldGenNyx(seed: Long) extends BWorldGen(seed, WorldGenNyx.defaultBlock) {
	private val noises = new NoisesNyx(seed)
	override protected val layers = List(
		new WorldGenLayerTerrain[NyxRegionTerrain](new NyxRegionTerrain(_, noises)),
		new NyxWorldGenLayerSnowAndIce(seed, 24)
	)
}
