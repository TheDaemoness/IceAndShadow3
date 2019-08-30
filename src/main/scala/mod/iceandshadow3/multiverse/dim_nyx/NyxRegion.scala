package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.gen.Cellmaker3d
import mod.iceandshadow3.lib.base.BWorldGenRegion
import mod.iceandshadow3.lib.spatial.{Cells, PairXZ}

class NyxRegion(coord: PairXZ, val noises: NoisesNyx)
extends BWorldGenRegion(coord) {
	val islemap = noises.isleMaker.apply(xFrom, zFrom, width, width)
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get

	lazy val fissuremapA = map3d(noises.fissuremakerMinor, WorldGenNyx.yFissureMax)
	lazy val fissuremapB = map3d(noises.fissuremakerMajor, WorldGenNyx.yFissureMax)
	lazy val cavemapA = map3d(noises.cavemakerA, WorldGenNyx.yCaveMax)
	lazy val cavemapB = map3d(noises.cavemakerB, WorldGenNyx.yCaveMax)

	lazy val heightmaps = new Object {
		val ridge = noises.noisemakerRidge.apply(xFrom, zFrom, width, width)
		val crater = noises.noisemakerDip.apply(xFrom, zFrom, width, width)
		val scale = noises.noisemakerRidgeScale.apply(xFrom, zFrom, width, width)
		val hill = noises.noisemakerHills.apply(xFrom, zFrom, width, width)
		val mountain = noises.noisemakerMountain.apply(xFrom, zFrom, width, width)
	}
	//lazy val stoneMapUpper = noises.stonemakerUpper.apply(xFrom, zFrom, width, width)
	lazy val stoneMapLower = noises.stonemakerLower.apply(xFrom, zFrom, width, width)

	private def map3d(in: Cellmaker3d, limit: Int) = {
		in(xFrom, 0, zFrom, width, limit, width, Cells.distance)
	}
}
