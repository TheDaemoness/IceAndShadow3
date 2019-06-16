package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.gen.BWorldSource

class WorldSourceNyx(seed: Long) extends BWorldSource {
	private val noises = new NoisesNyx(seed)

	override def getTerrainChunk(xFrom: Int, zFrom: Int, xWidth: Int, zWidth: Int) = {
		new ChunkSourceNyx(noises, xFrom,zFrom,xWidth,zWidth)
	}
}
