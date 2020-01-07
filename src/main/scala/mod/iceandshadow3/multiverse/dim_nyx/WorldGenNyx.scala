package mod.iceandshadow3.multiverse.dim_nyx

import java.util.Random

import com.google.common.cache.CacheLoader
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.gen.{BWorldGen, BWorldGenLayerTerrain}
import mod.iceandshadow3.lib.spatial.TupleXZ
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.dim_nyx.column.BNyxColumn
import mod.iceandshadow3.multiverse.dim_nyx.feature._
import mod.iceandshadow3.multiverse.gaia.ELivingstoneTypes

object WorldGenNyx {
	import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
	val stones = ELivingstoneTypes.values().map(st => {new WBlockState(livingstones(st.ordinal()))})
	val navistra = new WBlockState(navistra_stone)
	val bedrock = new WBlockState(navistra_bedrock)
	val icicles = new WBlockState(DomainNyx.Blocks.icicles)
	val exousia = new WBlockState(DomainNyx.Blocks.exousia)

	val yBald = 187
	val yThinning = 175
	val yFull = 167

	val yExousia = 8
	val yNavistraExtra = 2
	val yFissureFull = 147
	val yFissureMax = 171
	val yCaveMax = 156

	def stoneCommon(rng: Random) = stones(ELivingstoneTypes.getCommon(rng).ordinal())
	def stoneAny(rng: Random) = stones(ELivingstoneTypes.getAny(rng).ordinal())

	def defaultBlock(y: Int): WBlockState =
		if(y == 0) WorldGenNyx.bedrock else if(y <= yExousia) WorldGenNyx.exousia else CommonBlockTypes.AIR
}
final class WorldGenNyx(seed: Long) extends BWorldGen(seed, WorldGenNyx.defaultBlock) {
	private val noises = new NoisesNyx(seed)
	val islesinfo = com.google.common.cache.CacheBuilder.newBuilder().weakValues().build[TupleXZ, NyxIsleProperties](
		new CacheLoader[TupleXZ, NyxIsleProperties]{
			override def load(key: TupleXZ) = NyxIsleProperties(seed, key)
		}
	)
	private val terrain: BWorldGenLayerTerrain[BNyxColumn] = new BWorldGenLayerTerrain[BNyxColumn] {
		override protected def newGenerator(xFrom: Int, zFrom: Int, width: Int) =
			new NyxTerrainMaps(noises, xFrom, zFrom, width)
	}
	override protected val layers = Seq(
		terrain,
		new NyxLayerCrystals(seed, terrain),
		new NyxLayerTreesForest(seed, terrain, noises.getIsleInfo),
		new NyxLayerTreesScattered(seed, terrain, noises.getIsleInfo),
		new NyxWorldGenLayerSurface(seed, 24)
	)
}
