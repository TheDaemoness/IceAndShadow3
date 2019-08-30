package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.lib.base.BWorldGenChunk

class NyxChunk(xFrom: Int, zFrom: Int, source: NyxRegion)
extends BWorldGenChunk(xFrom, zFrom) {
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get

	def isle(x: Int, z: Int) = source.islemap(x, z)

	def fissure(x: Int, y: Int, z: Int) = {
		val fA = source.fissuremapA(x, y, z)
		Math.min(fA*fA, source.fissuremapB(x, y, z))
	}

	def cave(x: Int, y: Int, z: Int) = {
		source.cavemapA(x, y, z) * source.cavemapB(x, y, z)
	}

	def stoneLower(x: Int, z: Int) = source.stoneMapLower(x, z)

	def scale(x: Int, z: Int) = Math.sqrt(1-source.heightmaps.scale(x,z))
	def crater(x: Int, z: Int) = source.heightmaps.crater(x,z)
	def ridge(x: Int, z: Int) = source.heightmaps.ridge(x, z)
	def hill(x: Int, z: Int) = source.heightmaps.hill(x, z)
	def mountain(x: Int, z: Int) = source.heightmaps.mountain(x, z)

	val noises = source.noises
}
