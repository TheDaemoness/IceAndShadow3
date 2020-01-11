package mod.iceandshadow3.multiverse.dim_nyx

import com.google.common.cache.CacheLoader
import mod.iceandshadow3.lib.compat.block.WBlockState
import mod.iceandshadow3.lib.compat.block.`type`.CommonBlockTypes
import mod.iceandshadow3.lib.gen.{WorldGen, WorldGenLayerTerrain}
import mod.iceandshadow3.lib.spatial.TupleXZ
import mod.iceandshadow3.multiverse.DomainNyx
import mod.iceandshadow3.multiverse.dim_nyx.column.ColumnFnNyx
import mod.iceandshadow3.multiverse.dim_nyx.feature._

object WorldGenNyx {
	import mod.iceandshadow3.multiverse.DomainGaia.Blocks._
	val stones = livingstones.map(_.toWBlockState)
	val navistra = navistra_stone.toWBlockState
	val bedrock = navistra_bedrock.toWBlockState
	val icicles = DomainNyx.Blocks.icicles.toWBlockState
	val exousia = DomainNyx.Blocks.exousia.toWBlockState

	val yBald = 187
	val yThinning = 175
	val yFull = 167

	val yExousia = 8
	val yNavistraExtra = 2
	val yFissureFull = 147
	val yFissureMax = 171
	val yCaveMax = 156

	def defaultBlock(y: Int): WBlockState =
		if(y == 0) WorldGenNyx.bedrock else if(y <= yExousia) WorldGenNyx.exousia else CommonBlockTypes.AIR
}
final class WorldGenNyx(seed: Long) extends WorldGen(seed, WorldGenNyx.defaultBlock) {
	private val noises = new NoisesNyx(seed)
	import com.google.common.cache.CacheBuilder
	val islesinfo = CacheBuilder.newBuilder().weakValues().concurrencyLevel(
		Runtime.getRuntime.availableProcessors()
	).build[TupleXZ, NyxIsleProperties](new CacheLoader[TupleXZ, NyxIsleProperties]{
		override def load(key: TupleXZ) = NyxIsleProperties(seed, key)
	})
	private val terrain: WorldGenLayerTerrain[ColumnFnNyx] = new WorldGenLayerTerrain[ColumnFnNyx] {
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
