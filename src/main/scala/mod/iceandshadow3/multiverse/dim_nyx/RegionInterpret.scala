package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.IaS3
import mod.iceandshadow3.gen.Cellmaker3d
import mod.iceandshadow3.lib.base.BWorldGenRegion
import mod.iceandshadow3.lib.spatial.Cells
import mod.iceandshadow3.lib.util.collect.FixedMap2d

class RegionInterpret(xFrom: Int, zFrom: Int, val noises: NoisesNyx)
extends BWorldGenRegion(xFrom, zFrom) {
	val islemap = noises.isleMaker.apply(xFrom, zFrom, width, width)
	val smoothsnow = IaS3.getCfgServer.smooth_snow.get

	lazy val fissuremap = combine(noises.fissuremakerMinor, noises.fissuremakerMajor, WorldGenNyx.yFissureMax, (a, b) => {
		Math.min(a*a, b).toFloat
	})
	lazy val cavemap = combine(noises.cavemakerA, noises.cavemakerB, WorldGenNyx.yCaveMax, (a,b) => {
		(a*b).toFloat
	})
	val ridgeMap = noises.noisemakerRidge.apply(xFrom, zFrom, width, width)
	val craterMap = noises.noisemakerDip.apply(xFrom, zFrom, width, width)
	val scaleMap = noises.noisemakerRidgeScale.apply(xFrom, zFrom, width, width)
	val hillMap = noises.noisemakerHills.apply(xFrom, zFrom, width, width)
	val mountainMap = noises.noisemakerMountain.apply(xFrom, zFrom, width, width)

	private def combine(a: Cellmaker3d, b: Cellmaker3d, limit: Int, fn: (Double, Double) => Float) = {
		val resultsA = a(xFrom, 0, zFrom, width, limit, width, Cells.distance)
		val resultsB = b(xFrom, 0, zFrom, width, limit, width, Cells.distance)
		new FixedMap2d[Array[Float]](xFrom, zFrom, width, width, (x, z) => {
			Array.tabulate[Float](limit)(y => fn(resultsA(x, y, z), resultsB(x, y, z)))
		})
	}
}
