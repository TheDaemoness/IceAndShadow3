package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.gen.BWorldGenRegionTerrain
import mod.iceandshadow3.lib.spatial.PairXZ

class NyxRegionTerrain(coord: PairXZ, val noises: NoisesNyx)
extends BWorldGenRegionTerrain(coord) {
	override type F = BNyxColumn
	override protected def newGenerator() = new NyxTerrainMaps(noises, xFrom, zFrom)
}
